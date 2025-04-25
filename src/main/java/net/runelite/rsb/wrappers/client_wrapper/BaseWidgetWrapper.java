package net.runelite.rsb.wrappers.client_wrapper;

import net.runelite.api.FontTypeFace;
import net.runelite.api.Point;
import net.runelite.api.RenderOverview;
import net.runelite.api.widgets.Widget;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;

/*
Base class for wrapping runelite Widget.
*/
public abstract class BaseWidgetWrapper implements Widget {
    public final Widget wrappedWidget;

    public BaseWidgetWrapper(Widget widget) {
        this.wrappedWidget = widget;
    }

    @Override
    public int getId() {
        return wrappedWidget.getId();
    }

    @Override
    public int getType() {
        return wrappedWidget.getType();
    }

    @Override
    public void setType(int type) {
        wrappedWidget.setType(type);
    }

    @Override
    public int getContentType() {
        return wrappedWidget.getContentType();
    }

    @Override
    public Widget setContentType(int contentType) {
        return wrappedWidget.setContentType(contentType);
    }

    @Override
    public int getClickMask() {
        return wrappedWidget.getClickMask();
    }

    @Override
    public Widget setClickMask(int mask) {
        return wrappedWidget.setClickMask(mask);
    }

    @Override
    public Widget getParent() {
        return wrappedWidget.getParent();
    }

    @Override
    public int getParentId() {
        return wrappedWidget.getParentId();
    }

    @Override
    public Widget getChild(int index) {
        return wrappedWidget.getChild(index);
    }

    @Override
    @Nullable
    public Widget[] getChildren() {
        return wrappedWidget.getChildren();
    }

    @Override
    public void setChildren(Widget[] children) {
        wrappedWidget.setChildren(children);
    }

    @Override
    public Widget[] getDynamicChildren() {
        return wrappedWidget.getDynamicChildren();
    }

    @Override
    public Widget[] getStaticChildren() {
        return wrappedWidget.getStaticChildren();
    }

    @Override
    public Widget[] getNestedChildren() {
        return wrappedWidget.getNestedChildren();
    }

    @Override
    public int getRelativeX() {
        return wrappedWidget.getRelativeX();
    }

    @Override
    @Deprecated
    public void setRelativeX(int x) {
        wrappedWidget.setRelativeX(x);
    }

    @Override
    public int getRelativeY() {
        return wrappedWidget.getRelativeY();
    }

    @Override
    @Deprecated
    public void setRelativeY(int y) {
        wrappedWidget.setRelativeY(y);
    }

    @Override
    public void setForcedPosition(int x, int y) {
        wrappedWidget.setForcedPosition(x, y);
    }

    @Override
    public String getText() {
        return wrappedWidget.getText();
    }

    @Override
    public Widget setText(String text) {
        return wrappedWidget.setText(text);
    }

    @Override
    public int getTextColor() {
        return wrappedWidget.getTextColor();
    }

    @Override
    public Widget setTextColor(int textColor) {
        return wrappedWidget.setTextColor(textColor);
    }

    @Override
    public int getOpacity() {
        return wrappedWidget.getOpacity();
    }

    @Override
    public Widget setOpacity(int transparency) {
        return wrappedWidget.setOpacity(transparency);
    }

    @Override
    public String getName() {
        return wrappedWidget.getName();
    }

    @Override
    public Widget setName(String name) {
        return wrappedWidget.setName(name);
    }

    @Override
    public int getModelId() {
        return wrappedWidget.getModelId();
    }

    @Override
    public Widget setModelId(int id) {
        return wrappedWidget.setModelId(id);
    }

    @Override
    public int getModelType() {
        return wrappedWidget.getModelType();
    }

    @Override
    public Widget setModelType(int type) {
        return wrappedWidget.setModelType(type);
    }

    @Override
    public int getAnimationId() {
        return wrappedWidget.getAnimationId();
    }

    @Override
    public Widget setAnimationId(int animationId) {
        return wrappedWidget.setAnimationId(animationId);
    }

    @Override

    public int getRotationX() {
        return wrappedWidget.getRotationX();
    }

    @Override
    public Widget setRotationX(int modelX) {
        return wrappedWidget.setRotationX(modelX);
    }

    @Override

    public int getRotationY() {
        return wrappedWidget.getRotationY();
    }

    @Override
    public Widget setRotationY(int modelY) {
        return wrappedWidget.setRotationY(modelY);
    }

    @Override

    public int getRotationZ() {
        return wrappedWidget.getRotationZ();
    }

    @Override
    public Widget setRotationZ(int modelZ) {
        return wrappedWidget.setRotationZ(modelZ);
    }

    @Override
    public int getModelZoom() {
        return wrappedWidget.getModelZoom();
    }

    @Override
    public Widget setModelZoom(int modelZoom) {
        return wrappedWidget.setModelZoom(modelZoom);
    }

    @Override
    public int getSpriteId() {
        return wrappedWidget.getSpriteId();
    }

    @Override
    public boolean getSpriteTiling() {
        return wrappedWidget.getSpriteTiling();
    }

    @Override
    public Widget setSpriteTiling(boolean tiling) {
        return wrappedWidget.setSpriteTiling(tiling);
    }

    @Override
    public Widget setSpriteId(int spriteId) {
        return wrappedWidget.setSpriteId(spriteId);
    }

    @Override
    public boolean isHidden() {
        return wrappedWidget.isHidden();
    }

    @Override
    public boolean isSelfHidden() {
        return wrappedWidget.isSelfHidden();
    }

    @Override
    public Widget setHidden(boolean hidden) {
        return wrappedWidget.setHidden(hidden);
    }

    @Override
    public int getIndex() {
        return wrappedWidget.getIndex();
    }

    @Override
    public Point getCanvasLocation() {
        return wrappedWidget.getCanvasLocation();
    }

    @Override
    public int getWidth() {
        return wrappedWidget.getWidth();
    }

    @Override
    @Deprecated
    public void setWidth(int width) {
        wrappedWidget.setWidth(width);
    }

    @Override
    public int getHeight() {
        return wrappedWidget.getHeight();
    }

    @Override
    @Deprecated
    public void setHeight(int height) {
        wrappedWidget.setHeight(height);
    }

    @Override
    public Rectangle getBounds() {
        return wrappedWidget.getBounds();
    }
    @Override
    public int getItemId() {
        return wrappedWidget.getItemId();
    }

    @Override
    public Widget setItemId(int itemId) {
        return wrappedWidget.setItemId(itemId);
    }

    @Override
    public int getItemQuantity() {
        return wrappedWidget.getItemQuantity();
    }

    @Override
    public Widget setItemQuantity(int quantity) {
        return wrappedWidget.setItemQuantity(quantity);
    }

    @Override
    public boolean contains(Point point) {
        return wrappedWidget.contains(point);
    }

    @Override
    public int getScrollX() {
        return wrappedWidget.getScrollX();
    }

    @Override
    public Widget setScrollX(int scrollX) {
        return wrappedWidget.setScrollX(scrollX);
    }

    @Override
    public int getScrollY() {
        return wrappedWidget.getScrollY();
    }

    @Override
    public Widget setScrollY(int scrollY) {
        return wrappedWidget.setScrollY(scrollY);
    }

    @Override
    public int getScrollWidth() {
        return wrappedWidget.getScrollWidth();
    }

    @Override
    public Widget setScrollWidth(int width) {
        return wrappedWidget.setScrollWidth(width);
    }

    @Override
    public int getScrollHeight() {
        return wrappedWidget.getScrollHeight();
    }

    @Override
    public Widget setScrollHeight(int height) {
        return wrappedWidget.setScrollHeight(height);
    }

    @Override
    public int getOriginalX() {
        return wrappedWidget.getOriginalX();
    }

    @Override
    public Widget setOriginalX(int originalX) {
        return wrappedWidget.setOriginalX(originalX);
    }

    @Override
    public int getOriginalY() {
        return wrappedWidget.getOriginalY();
    }

    @Override
    public Widget setOriginalY(int originalY) {
        return wrappedWidget.setOriginalY(originalY);
    }

    @Override
    public Widget setPos(int x, int y) {
        return wrappedWidget.setPos(x, y);
    }

    @Override
    public Widget setPos(int x, int y, int xMode, int yMode) {
        return wrappedWidget.setPos(x, y, xMode, yMode);
    }

    @Override
    public int getOriginalHeight() {
        return wrappedWidget.getOriginalHeight();
    }

    @Override
    public Widget setOriginalHeight(int originalHeight) {
        return wrappedWidget.setOriginalHeight(originalHeight);
    }

    @Override
    public int getOriginalWidth() {
        return wrappedWidget.getOriginalWidth();
    }

    @Override
    public Widget setOriginalWidth(int originalWidth) {
        return wrappedWidget.setOriginalWidth(originalWidth);
    }

    @Override
    public Widget setSize(int width, int height) {
        return wrappedWidget.setSize(width, height);
    }

    @Override
    public Widget setSize(int width, int height, int widthMode, int heightMode) {
        return wrappedWidget.setSize(width, height, widthMode, heightMode);
    }

    @Override
    public String[] getActions() {
        return wrappedWidget.getActions();
    }

    @Override
    public Widget createChild(int index, int type) {
        return wrappedWidget.createChild(index, type);
    }

    @Override
    public Widget createChild(int type) {
        return wrappedWidget.createChild(type);
    }

    @Override
    public void deleteAllChildren() {
        wrappedWidget.deleteAllChildren();
    }

    @Override
    public void setAction(int index, String action) {
        wrappedWidget.setAction(index, action);
    }

    @Override
    public void setOnOpListener(Object... args) {
        wrappedWidget.setOnOpListener(args);
    }

    @Override
    public void setOnDialogAbortListener(Object... args) {
        wrappedWidget.setOnDialogAbortListener(args);
    }

    @Override
    public void setOnKeyListener(Object... args) {
        wrappedWidget.setOnKeyListener(args);
    }

    @Override
    public void setOnMouseOverListener(Object... args) {
        wrappedWidget.setOnMouseOverListener(args);
    }

    @Override
    public void setOnMouseRepeatListener(Object... args) {
        wrappedWidget.setOnMouseRepeatListener(args);
    }

    @Override
    public void setOnMouseLeaveListener(Object... args) {
        wrappedWidget.setOnMouseLeaveListener(args);
    }

    @Override
    public void setOnTimerListener(Object... args) {
        wrappedWidget.setOnTimerListener(args);
    }

    @Override
    public void setOnTargetEnterListener(Object... args) {
        wrappedWidget.setOnTargetEnterListener(args);
    }

    @Override
    public void setOnTargetLeaveListener(Object... args) {
        wrappedWidget.setOnTargetLeaveListener(args);
    }

    @Override
    public boolean hasListener() {
        return wrappedWidget.hasListener();
    }

    @Override
    public Widget setHasListener(boolean hasListener) {
        return wrappedWidget.setHasListener(hasListener);
    }

    @Override
    public boolean isIf3() {
        return wrappedWidget.isIf3();
    }

    @Override
    public void revalidate() {
        wrappedWidget.revalidate();
    }

    @Override
    public void revalidateScroll() {
        wrappedWidget.revalidateScroll();
    }

    @Override
    public Object[] getOnOpListener() {
        return wrappedWidget.getOnOpListener();
    }

    @Override
    public Object[] getOnKeyListener() {
        return wrappedWidget.getOnKeyListener();
    }

    @Override
    public Object[] getOnLoadListener() {
        return wrappedWidget.getOnLoadListener();
    }

    @Override
    public Object[] getOnInvTransmitListener() {
        return wrappedWidget.getOnInvTransmitListener();
    }

    @Override
    public int getFontId() {
        return wrappedWidget.getFontId();
    }

    @Override
    public Widget setFontId(int id) {
        return wrappedWidget.setFontId(id);
    }

    @Override
    public int getBorderType() {
        return wrappedWidget.getBorderType();
    }

    @Override
    public void setBorderType(int thickness) {
        wrappedWidget.setBorderType(thickness);
    }

    @Override
    public boolean getTextShadowed() {
        return wrappedWidget.getTextShadowed();
    }

    @Override
    public Widget setTextShadowed(boolean shadowed) {
        return wrappedWidget.setTextShadowed(shadowed);
    }

    @Override
    public int getDragDeadZone() {
        return wrappedWidget.getDragDeadZone();
    }

    @Override
    public void setDragDeadZone(int deadZone) {
        wrappedWidget.setDragDeadZone(deadZone);
    }

    @Override
    public int getDragDeadTime() {
        return wrappedWidget.getDragDeadTime();
    }

    @Override
    public void setDragDeadTime(int deadTime) {
        wrappedWidget.setDragDeadTime(deadTime);
    }

    @Override
    public int getItemQuantityMode() {
        return wrappedWidget.getItemQuantityMode();
    }

    @Override
    public Widget setItemQuantityMode(int itemQuantityMode) {
        return wrappedWidget.setItemQuantityMode(itemQuantityMode);
    }

    @Override
    public int getXPositionMode() {
        return wrappedWidget.getXPositionMode();
    }

    @Override
    public Widget setXPositionMode(int xpm) {
        return wrappedWidget.setXPositionMode(xpm);
    }

    @Override
    public int getYPositionMode() {
        return wrappedWidget.getYPositionMode();
    }

    @Override
    public Widget setYPositionMode(int ypm) {
        return wrappedWidget.setYPositionMode(ypm);
    }

    @Override
    public int getLineHeight() {
        return wrappedWidget.getLineHeight();
    }

    @Override
    public Widget setLineHeight(int lineHeight) {
        return wrappedWidget.setLineHeight(lineHeight);
    }

    @Override
    public int getXTextAlignment() {
        return wrappedWidget.getXTextAlignment();
    }

    @Override
    public Widget setXTextAlignment(int xta) {
        return wrappedWidget.setXTextAlignment(xta);
    }

    @Override
    public int getYTextAlignment() {
        return wrappedWidget.getYTextAlignment();
    }

    @Override
    public Widget setYTextAlignment(int yta) {
        return wrappedWidget.setYTextAlignment(yta);
    }

    @Override
    public int getWidthMode() {
        return wrappedWidget.getWidthMode();
    }

    @Override
    public Widget setWidthMode(int widthMode) {
        return wrappedWidget.setWidthMode(widthMode);
    }

    @Override
    public int getHeightMode() {
        return wrappedWidget.getHeightMode();
    }

    @Override
    public Widget setHeightMode(int heightMode) {
        return wrappedWidget.setHeightMode(heightMode);
    }

    @Override
    public FontTypeFace getFont() {
        return wrappedWidget.getFont();
    }

    @Override
    public boolean isFilled() {
        return wrappedWidget.isFilled();
    }

    @Override
    public Widget setFilled(boolean filled) {
        return wrappedWidget.setFilled(filled);
    }

    @Override
    public String getTargetVerb() {
        return wrappedWidget.getTargetVerb();
    }

    @Override
    public void setTargetVerb(String targetVerb) {
        wrappedWidget.setTargetVerb(targetVerb);
    }

    @Override
    public boolean getNoClickThrough() {
        return wrappedWidget.getNoClickThrough();
    }

    @Override
    public void setNoClickThrough(boolean noClickThrough) {
        wrappedWidget.setNoClickThrough(noClickThrough);
    }

    @Override
    public boolean getNoScrollThrough() {
        return wrappedWidget.getNoScrollThrough();
    }

    @Override
    public void setNoScrollThrough(boolean noScrollThrough) {
        wrappedWidget.setNoScrollThrough(noScrollThrough);
    }

    @Override
    public void setVarTransmitTrigger(final int... trigger) {
        wrappedWidget.setVarTransmitTrigger(trigger);
    }

    @Override
    public void setOnClickListener(Object... args) {
        wrappedWidget.setOnClickListener(args);
    }

    @Override
    public void setOnHoldListener(Object... args) {
        wrappedWidget.setOnHoldListener(args);
    }

    @Override
    public void setOnReleaseListener(Object... args) {
        wrappedWidget.setOnReleaseListener(args);
    }

    @Override
    public void setOnDragCompleteListener(Object... args) {
        wrappedWidget.setOnDragCompleteListener(args);
    }

    @Override
    public void setOnDragListener(Object... args) {
        wrappedWidget.setOnDragListener(args);
    }

    @Override
    public Widget getDragParent() {
        return wrappedWidget.getDragParent();
    }

    @Override
    public Widget setDragParent(Widget dragParent) {
        return wrappedWidget.setDragParent(dragParent);
    }

    @Override
    public Object[] getOnVarTransmitListener() {
        return wrappedWidget.getOnVarTransmitListener();
    }

    @Override
    public void setOnVarTransmitListener(Object... args) {
        wrappedWidget.setOnVarTransmitListener(args);
    }

    @Override
    public void setOnScrollWheelListener(Object... objects) {
        wrappedWidget.setOnScrollWheelListener(objects);
    }

    @Override
    public void clearActions() { wrappedWidget.clearActions(); }

    @Override
    public int[] getVarTransmitTrigger() {
        return wrappedWidget.getVarTransmitTrigger();
    }
}
