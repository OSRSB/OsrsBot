package net.runelite.rsb.wrappers.client_wrapper;

import lombok.SneakyThrows;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;

/*
Wrapper around runelite Client, adds synchronization with ClientThread for methods that require it.
In order to synchronize Widget methods, all returned Widget instances are also wrapped.
Running methods on client thread comes with a performance hit, so only methods that are confirmed to cause problems are synchronized.
*/
public class RSClient extends BaseClientWrapper {
    private final Queue<FutureTask<Object>> taskQueue = new ConcurrentLinkedQueue<>();

    public RSClient(Client client, ClientThread clientThread) {
        super(client);
        clientThread.invoke(() -> {
            final var expirationTime = Instant.now().plusMillis(20);
            if (taskQueue.isEmpty()) return false;
            while (Instant.now().isBefore(expirationTime)) {
                final var task = taskQueue.poll();
                if (task != null) {
                    task.run();
                }
            }
            return false;
        });
    }

    private void runTask(FutureTask<Object> task) {
        if (super.isClientThread()) {
            task.run();
        } else {
            taskQueue.add(task);
        }
    }

    @SneakyThrows
    private void runOnClientThread(Runnable method) {
        final var task = new FutureTask<Object>(() -> { method.run();return null; } );
        runTask(task);
        task.get();
    }

    private <T> T convertResult(T result) {
        if (result instanceof Widget concreteResult) {
            return (T) new WidgetWrapper(concreteResult);
        } else if (result instanceof Widget[] concreteResult) {
            WidgetWrapper[] convertedResult = new WidgetWrapper[concreteResult.length];
            for (int i = 0 ; i < concreteResult.length ; i++) {
                convertedResult[i] = new WidgetWrapper(concreteResult[i]);
            }
            return (T) convertedResult;
        }
        return result;
    }

    private Widget[] convertArg(Widget[] arg) {
        Widget[] convertedArg = new Widget[arg.length];
        for (int i = 0 ; i < arg.length ; i++) {
            if (arg[i] instanceof WidgetWrapper widgetWrapper) {
                convertedArg[i] = widgetWrapper.wrappedWidget;
            } else {
                convertedArg[i] = arg[i];
            }
        }
        return convertedArg;
    }

    @SneakyThrows
    private <T> T runOnClientThread(Callable<T> method) {
        final var task = new FutureTask<Object>(() -> convertResult(method.call()));
        runTask(task);
        return (T) task.get();
    }

    @Override
    @Nullable
    public Widget getDraggedWidget() { // tested, no need to runOnClientThread
        return convertResult(super.getDraggedWidget());
    }

    @Override
    @Nullable
    public Widget getDraggedOnWidget() { // tested, no need to runOnClientThread
        return convertResult(super.getDraggedOnWidget());
    }

    @Override
    public void setDraggedOnWidget(Widget widget) {
        super.setDraggedOnWidget(((WidgetWrapper) widget).wrappedWidget);
    }

    @Override
    public Widget[] getWidgetRoots() { // tested, no need to runOnClientThread
        return convertResult(super.getWidgetRoots());
    }

    @SneakyThrows
    @Override
    @Nullable
    public Widget getWidget(WidgetInfo widget) { // tested, no need to runOnClientThread
        return convertResult(super.getWidget(widget));
    }

    @Override
    @Nullable
    public Widget getWidget(int groupId, int childId) { // tested, no need to runOnClientThread
        return convertResult(super.getWidget(groupId, childId));
    }

    @Override
    @Nullable
    public Widget getWidget(int packedID) { // tested, no need to runOnClientThread
        return convertResult(super.getWidget(packedID));
    }

    @Override
    public Widget getScriptActiveWidget() {
        return convertResult(super.getScriptActiveWidget());
    }

    @Override
    public Widget getScriptDotWidget() {
        return convertResult(super.getScriptDotWidget());
    }

    @Override
    @Deprecated
    public Widget getIf1DraggedWidget() {
        return convertResult(super.getIf1DraggedWidget());
    }

    @Override
    @Nullable
    public Widget getSelectedWidget() {
        return convertResult(super.getSelectedWidget());
    }

    @Nullable
    @Override
    public LocalPoint getLocalDestinationLocation() { // tested, causes freezes without runOnClientThread
        return runOnClientThread(super::getLocalDestinationLocation);
    }

    @Override
    public int getSceneMaxPlane() {
        return 0;
    }

    @Override
    public Player getLocalPlayer() { // tested, causes freezes without runOnClientThread
        return runOnClientThread(super::getLocalPlayer);
    }

    @Nullable
    @Override
    public CollisionData[] getCollisionMaps() { // tested, causes freezes without runOnClientThread
        return runOnClientThread(super::getCollisionMaps);
    }

    private class WidgetWrapper extends BaseWidgetWrapper {

        WidgetWrapper(Widget widget) {
            super(widget);
        }

        @Override
        public Widget getParent() { // tested, causes freezes without runOnClientThread
            return runOnClientThread(super::getParent);
        }

        @Override
        public int getParentId() { // tested, causes freezes without runOnClientThread
            return runOnClientThread(super::getParentId);
        }

        @Override
        public Widget setContentType(int contentType) {
            return convertResult(super.setContentType(contentType));
        }

        @Override
        public Widget setClickMask(int mask) {
            return convertResult(super.setClickMask(mask));
        }

        @Override
        public Widget getChild(int index) {
            return convertResult(super.getChild(index));
        }

        @Override
        @Nullable
        public Widget[] getChildren() {
            return convertResult(super.getChildren());
        }

        @Override
        public void setChildren(Widget[] children) {
            super.setChildren(convertArg(children));
        }

        @Override
        public Widget[] getDynamicChildren() { // tested, no need to runOnClientThread
            return convertResult(super.getDynamicChildren());
        }

        @Override
        public Widget[] getStaticChildren() { // tested, no need to runOnClientThread
            return convertResult(super.getStaticChildren());
        }

        @Override
        public Widget[] getNestedChildren() { // tested, no need to runOnClientThread
            return convertResult(super.getNestedChildren());
        }

        @Override
        public Widget setText(String text) {
            return convertResult(super.setText(text));
        }

        @Override
        public Widget setTextColor(int textColor) {
            return convertResult(super.setTextColor(textColor));
        }

        @Override
        public Widget setOpacity(int transparency) {
            return convertResult(super.setOpacity(transparency));
        }

        @Override
        public Widget setName(String name) {
            return convertResult(super.setName(name));
        }

        @Override
        public Widget setModelId(int id) {
            return convertResult(super.setModelId(id));
        }

        @Override
        public Widget setModelType(int type) {
            return convertResult(super.setModelType(type));
        }

        @Override
        public Widget setAnimationId(int animationId) {
            return convertResult(super.setAnimationId(animationId));
        }

        @Override
        public Widget setRotationX(int modelX) {
            return convertResult(super.setRotationX(modelX));
        }

        @Override
        public Widget setRotationY(int modelY) {
            return convertResult(super.setRotationY(modelY));
        }

        @Override
        public Widget setRotationZ(int modelZ) {
            return convertResult(super.setRotationZ(modelZ));
        }

        @Override
        public Widget setModelZoom(int modelZoom) {
            return convertResult(super.setModelZoom(modelZoom));
        }

        @Override
        public Widget setSpriteTiling(boolean tiling) {
            return convertResult(super.setSpriteTiling(tiling));
        }

        @Override
        public Widget setSpriteId(int spriteId) {
            return convertResult(super.setSpriteId(spriteId));
        }

        @Override
        public Widget setHidden(boolean hidden) {
            return convertResult(super.setHidden(hidden));
        }

        @Override
        public Widget setItemId(int itemId) {
            return convertResult(super.setItemId(itemId));
        }

        @Override
        public Widget setItemQuantity(int quantity) {
            return convertResult(super.setItemQuantity(quantity));
        }

        @Override
        public Widget setScrollX(int scrollX) {
            return convertResult(super.setScrollX(scrollX));
        }

        @Override
        public Widget setScrollY(int scrollY) {
            return convertResult(super.setScrollY(scrollY));
        }

        @Override
        public Widget setScrollWidth(int width) {
            return convertResult(super.setScrollWidth(width));
        }

        @Override
        public Widget setScrollHeight(int height) {
            return convertResult(super.setScrollHeight(height));
        }

        @Override
        public Widget setOriginalX(int originalX) {
            return convertResult(super.setOriginalX(originalX));
        }

        @Override
        public Widget setOriginalY(int originalY) {
            return convertResult(super.setOriginalY(originalY));
        }

        @Override
        public Widget setPos(int x, int y) {
            return convertResult(super.setPos(x, y));
        }

        @Override
        public Widget setPos(int x, int y, int xMode, int yMode) {
            return convertResult(super.setPos(x, y, xMode, yMode));
        }

        @Override
        public Widget setOriginalHeight(int originalHeight) {
            return convertResult(super.setOriginalHeight(originalHeight));
        }

        @Override
        public Widget setOriginalWidth(int originalWidth) {
            return convertResult(super.setOriginalWidth(originalWidth));
        }

        @Override
        public Widget setSize(int width, int height) {
            return convertResult(super.setSize(width, height));
        }

        @Override
        public Widget setSize(int width, int height, int widthMode, int heightMode) {
            return convertResult(super.setSize(width, height, widthMode, heightMode));
        }

        @Override
        public Widget createChild(int index, int type) {
            return convertResult(super.createChild(index, type));
        }

        @Override
        public Widget createChild(int type) {
            return convertResult(super.createChild(type));
        }

        @Override
        public Widget setHasListener(boolean hasListener) {
            return convertResult(super.setHasListener(hasListener));
        }

        @Override
        public Widget setFontId(int id) {
            return convertResult(super.setFontId(id));
        }

        @Override
        public Widget setTextShadowed(boolean shadowed) {
            return convertResult(super.setTextShadowed(shadowed));
        }

        @Override
        public Widget setItemQuantityMode(int itemQuantityMode) {
            return convertResult(super.setItemQuantityMode(itemQuantityMode));
        }

        @Override
        public Widget setXPositionMode(int xpm) {
            return convertResult(super.setXPositionMode(xpm));
        }

        @Override
        public Widget setYPositionMode(int ypm) {
            return convertResult(super.setYPositionMode(ypm));
        }

        @Override
        public Widget setLineHeight(int lineHeight) {
            return convertResult(super.setLineHeight(lineHeight));
        }

        @Override
        public Widget setXTextAlignment(int xta) {
            return convertResult(super.setXTextAlignment(xta));
        }

        @Override
        public Widget setYTextAlignment(int yta) {
            return convertResult(super.setYTextAlignment(yta));
        }

        @Override
        public Widget setWidthMode(int widthMode) {
            return convertResult(super.setWidthMode(widthMode));
        }

        @Override
        public Widget setHeightMode(int heightMode) {
            return convertResult(super.setHeightMode(heightMode));
        }

        @Override
        public Widget setFilled(boolean filled) {
            return convertResult(super.setFilled(filled));
        }

        @Override
        public Widget getDragParent() {
            return convertResult(super.getDragParent());
        }

        @Override
        public Widget setDragParent(Widget dragParent) {
            return convertResult(super.setDragParent(dragParent));
        }
    }
}
