package net.runelite.rsb.wrappers.common;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.util.StdRandom;
import net.runelite.api.Point;

import java.awt.*;
import java.awt.geom.PathIterator;


import static net.runelite.rsb.methods.MethodProvider.methods;
@Slf4j
public class ClickBox implements Clickable07, PaintListener {
    Clickable07 clickable;
    Shape shape;
    long lastShapeUpdate = 0;

    public ClickBox(Clickable07 clickable) {
        this.clickable = clickable;
    }

    @Override
    public boolean doAction(String action) {
        return doAction(action, null);
    }

    @Override
    public boolean doAction(String action, String option) {
        for (int i = 0; i < 3; i++) {
            if (!contains(methods.mouse.getLocation())) {
                doHover();
            }
            if (methods.menu.doAction(action, option)) {
                return true;
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
        for (int i = 0; i < 3; i++) {
            if (!contains(methods.mouse.getLocation())) {
                doHover();
            }
            if (contains(methods.mouse.getLocation())) {
                methods.mouse.click(leftClick);
                return true;
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
        if (shape == null || System.currentTimeMillis() - lastShapeUpdate > 50) {
            shape = clickable.getClickShape();
            lastShapeUpdate = System.currentTimeMillis();
        }
        return shape;
    }

    @Override
    public ClickBox getClickBox() {
        return clickable.getClickBox();
    }

    @Override
    public boolean isClickable() {
        return clickable.isClickable();
    }

    public boolean contains(Point point) {
        Shape shape = getClickShape();
        if (shape != null) {
            return shape.contains(point.getX(), point.getY());
        }
        return false;
    }

    /**
     * Gets a random point within the bounds of the shape by repeated sampling.
     * @return A random point within the bounds of the shape
     */
    public Point getRandomPoint() {
        Shape shape = getClickShape();
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            for (int j = 0; j < 100; j++) {
                int x = (int) bounds.getX() + StdRandom.uniform((int) bounds.getWidth());
                int y = (int) bounds.getY() + StdRandom.uniform((int) bounds.getHeight());
                if (shape.contains(x, y)) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Gets the offset of the point from the top left corner of the bounds.
     * @param point The point to get the offset of
     * @return The offset of the point from the top left corner of the bounds
     */
    public Point getOffset(Point point) {
        Shape shape = getClickShape();
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            int x = point.getX();
            int y = point.getY();
            if (shape.contains(x, y)) {
                return new Point(x - (int) bounds.getX(), y - (int) bounds.getY());
            }
        }
        return null;
    }

    /**
     * Gets the point from the offset of the top left corner of the bounds.
     * @param offset The offset of the point from the top left corner of the bounds
     * @return The point from the offset of the top left corner of the bounds
     */
    public Point getPointFromOffset(Point offset) {
        Shape shape = getClickShape();
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

    /**
     * Gets the barycentric centroid of the shape.
     * @return The centroid of the shape
     */
    public Point getCenterPoint() {
        Shape shape = getClickShape();
        if (shape == null) {
            return null;
        }
        final double flatness = 0.1;
        PathIterator pi = shape.getPathIterator(null, flatness);
        double coords[] = new double[6];
        double sumX = 0;
        double sumY = 0;
        int numPoints = 0;
        while (!pi.isDone())
        {
            int s = pi.currentSegment(coords);
            switch (s)
            {
                case PathIterator.SEG_MOVETO:
                    // Ignore
                    break;

                case PathIterator.SEG_LINETO:
                    sumX += coords[0];
                    sumY += coords[1];
                    numPoints++;
                    break;

                case PathIterator.SEG_CLOSE:
                    // Ignore
                    break;

                case PathIterator.SEG_QUADTO:
                    throw new AssertionError(
                            "SEG_QUADTO in flattening path iterator");
                case PathIterator.SEG_CUBICTO:
                    throw new AssertionError(
                            "SEG_CUBICTO in flattening path iterator");
            }
            pi.next();
        }
        double x = sumX / numPoints;
        double y = sumY / numPoints;
        return new Point((int) x,(int) y);
    }

    public Point getPointNearCenter() {
        return getPointNearCenter(5);
    }

    /**
     * Generates attempts number of points and returns the farthest point from any edge.
     * To generate the points it repeatedly samples a gaussian around the center using the distance to the nearest
     * edge to get variance until a point within the shape is found.
     *
     * This has some problems. It is possible that the shape is not convex and the center is not actually within the
     * shape. This means the gaussian will be outside the shape the majority of the time.
     *
     * @param attempts The number of points to generate
     * @return The farthest point from any edge
     */
    public Point getPointNearCenter(int attempts) {
        Point center = getCenterPoint();
        Shape shape = getClickShape();
        if (center == null || shape == null) {
            return null;
        }
        double centerDistanceFromEdge = getDistanceFromEdge(center);
        int centerX = center.getX();
        int centerY = center.getY();

        Point bestPoint = null;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < attempts; i++) {
            for (int j = 0; j < 100; j++) {
                int x = (int) StdRandom.gaussian(centerX, (centerDistanceFromEdge) / 3);
                int y = (int) StdRandom.gaussian(centerY, (centerDistanceFromEdge) / 3);
                if (shape.contains(x, y)) {
                    double distance = getDistanceFromEdge(x,y);
                    if (!Double.isNaN(distance) && distance < bestDistance) {
                        bestPoint = new Point(x, y);
                        bestDistance = distance;
                    }
                    break;
                }
            }
        }
        return bestPoint;
    }

    private double getDistanceFromEdge(int x, int y) {
        Shape shape = getClickShape();
        if (shape == null) {
            return Double.NaN;
        }
        final double flatness = 0.1;
        PathIterator pi = shape.getPathIterator(null, flatness);
        double prevCoords[] = new double[6];
        double coords[] = new double[6];
        double minDistance = Double.MAX_VALUE;
        while (!pi.isDone()) {
            int s = pi.currentSegment(coords);
            switch (s) {
                case PathIterator.SEG_MOVETO:
                    prevCoords[0] = coords[0];
                    prevCoords[1] = coords[1];
                    break;

                case PathIterator.SEG_LINETO:
                    double distance = distanceToLineSegment(x, y, prevCoords[0], prevCoords[1], coords[0], coords[1]);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                    prevCoords[0] = coords[0];
                    prevCoords[1] = coords[1];
                    break;

                case PathIterator.SEG_CLOSE:
                    // Ignore
                    break;

                case PathIterator.SEG_QUADTO:
                    throw new AssertionError(
                            "SEG_QUADTO in flattening path iterator");
                case PathIterator.SEG_CUBICTO:
                    throw new AssertionError(
                            "SEG_CUBICTO in flattening path iterator");
            }
            pi.next();
        }
        return minDistance;
    }

    private double getDistanceFromEdge(Point point) {
        return getDistanceFromEdge(point.getX(), point.getY());
    }

    private double distanceToLineSegment(double x, double y, double xStart, double yStart, double xEnd, double yEnd) {
        // http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
        double l2 = Math.pow(xEnd - xStart, 2) + Math.pow(yEnd - yStart, 2);
        if (l2 == 0) {
            return Math.sqrt(Math.pow(x - xStart, 2) + Math.pow(y - yStart, 2));
        }
        double t = Math.max(0, Math.min(1, ((x - xStart) * (xEnd - xStart) + (y - yStart) * (yEnd - yStart)) / l2));
        double xProj = xStart + t * (xEnd - xStart);
        double yProj = yStart + t * (yEnd - yStart);
        return Math.sqrt(Math.pow(x - xProj, 2) + Math.pow(y - yProj, 2));
    }

    @Override
    public void onRepaint(Graphics render) {
        lastShapeUpdate = System.currentTimeMillis();
        shape = clickable.getClickShape();
    }
}
