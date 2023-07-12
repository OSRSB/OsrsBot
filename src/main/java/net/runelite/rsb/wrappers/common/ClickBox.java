package net.runelite.rsb.wrappers.common;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.util.StdRandom;
import net.runelite.api.Point;

import java.awt.Shape;
import java.awt.Rectangle;


import static net.runelite.rsb.methods.MethodProvider.methods;
@Slf4j
public class ClickBox implements Clickable07 {
    Clickable07 clickable;

    public ClickBox(Clickable07 clickable) {
        this.clickable = clickable;
    }

    @Override
    public boolean doAction(String action) {
        return doAction(action, null);
    }

    @Override
    public boolean doAction(String action, String option) {
        Point point = getRandomPoint();
        if (point != null && isClickable()) {
            for (int i = 0; i < 3; i++) {
                if (!contains(methods.mouse.getLocation())) {
                    methods.mouse.move(point);
                }
                if (methods.menu.doAction(action, option)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean doClick() {
        return doClick(true);
    }

    @Override
    public boolean doClick(boolean leftClick) {
        Point point = getRandomPoint();
        if (point != null && isClickable()) {
            for (int i = 0; i < 3; i++) {
                if (!contains(methods.mouse.getLocation())) {
                    methods.mouse.move(point);
                }
                if (contains(methods.mouse.getLocation())) {
                    methods.mouse.click(leftClick);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean doHover() {
        Point point = getRandomPoint();
        if (point != null && isClickable()) {
            for (int i = 0; i < 3; i++) {
                if (!contains(methods.mouse.getLocation())) {
                    methods.mouse.move(point);
                }
                if (contains(methods.mouse.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Shape getClickShape() {
        return clickable.getClickShape();
    }

    @Override
    public ClickBox getClickBox() {
        return clickable.getClickBox();
    }

    public boolean contains(Point point) {
        Shape shape = clickable.getClickShape();
        if (shape != null) {
            return shape.contains(point.getX(), point.getY());
        }
        return false;
    }

    public Point getRandomPoint() {
        Shape shape = clickable.getClickShape();
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            while (true) {
                int x = (int) bounds.getX() + StdRandom.uniform((int) bounds.getWidth());
                int y = (int) bounds.getY() + StdRandom.uniform((int) bounds.getHeight());
                if (shape.contains(x, y)) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public Point getRandomOffset() {
        Shape shape = clickable.getClickShape();
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            while (true) {
                int x = (int) bounds.getX() + StdRandom.uniform((int) bounds.getWidth());
                int y = (int) bounds.getY() + StdRandom.uniform((int) bounds.getHeight());
                if (shape.contains(x, y)) {
                    return new Point(x - (int) bounds.getX(), y - (int) bounds.getY());
                }
            }
        }
        return null;
    }

    public Point getPointFromOffset(Point offset) {
        Shape shape = clickable.getClickShape();
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            int x = (int) bounds.getX() + offset.getX();
            int y = (int) bounds.getY() + offset.getY();
            if (shape.contains(x, y)) {
                return new Point(x, y);
            }
        }
        return null;
    }

    @Override
    public boolean isClickable() {
        return clickable.isClickable();
    }
}
