package net.runelite.rsb.event.impl;

import net.runelite.api.Point;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.methods.MethodContext;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.ArrayDeque;
import java.util.Iterator;


public class DrawMouseTrail implements PaintListener {
    private final MethodContext ctx;
    private final ArrayDeque<Point> mousePoints;
    private final int maxTrailLength;
    private final Color colour;

    public DrawMouseTrail(final BotLite bot) {
        this(bot, 7, Color.GREEN);
    }

    public DrawMouseTrail(final BotLite bot, final int maxTrailLength, final Color colour) {
        this.ctx = bot.getMethodContext();
        this.maxTrailLength = maxTrailLength;
        this.colour = colour;
        mousePoints = new ArrayDeque<>(maxTrailLength);
    }

    @Override
    public void onRepaint(Graphics render) {
        updatePoints();
        if (mousePoints.size() > 1) drawPath((Graphics2D) render);
    }

    private void updatePoints() {
        Point mousePos = ctx.mouse.getLocation();
        if (mousePos.equals(mousePoints.peekLast())) {
            mousePoints.poll();
            return;
        }
        if (mousePoints.size() >= maxTrailLength) mousePoints.poll();
        if (mousePos.getX() < 0 || mousePos.getY() < 0) {
            if (mousePoints.size() > 0) mousePoints.offer(mousePoints.peekLast());
        } else mousePoints.offer(mousePos);
    }

    private void drawPath(final Graphics2D g) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(colour);

        GeneralPath path = new GeneralPath();

        Iterator<Point> mousePointIterator = mousePoints.iterator();

        Point firstPoint = mousePointIterator.next();
        path.moveTo(firstPoint.getX(), firstPoint.getY());

        Point prev = firstPoint;
        while (mousePointIterator.hasNext()) {
            Point current = mousePointIterator.next();
            path.quadTo(prev.getX(), prev.getY(), current.getX(), current.getY());
            prev = current;
        }
        g.draw(path);
    }
}
