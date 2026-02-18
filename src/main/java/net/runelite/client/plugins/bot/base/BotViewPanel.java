package net.runelite.client.plugins.bot.base;

import net.runelite.client.ui.PluginPanel;
import net.runelite.rsb.botLauncher.Application;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.botLauncher.BotLiteInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BotViewPanel extends PluginPanel {

    JButton reloadButton = new JButton("Reload");

    public BotViewPanel() {
        super();
        reloadButton.addActionListener(e -> {
            paint(getGraphics());
        });
        this.add(reloadButton);
    }

    // TODO: Replace; temporary preview.
    public void paint(Graphics g) {
        BotLiteInterface[] bots = Application.getBots();
        int width = getWidth(), height = getHeight();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        int len = Math.min(bots.length, 6);
        if (len == 1) {
            draw(g, 0, 0, 0, width, height);
        } else if (len == 2) {
            draw(g, 0, 0, 0, width, height / 2);
            draw(g, 1, 0, height / 2, width, height / 2);
        } else if (len == 3) {
            draw(g, 0, 0, 0, width / 2, height / 2);
            draw(g, 1, width / 2, 0, width / 2, height / 2);
            draw(g, 2, 0, height / 2, width, height / 2);
        } else if (len == 4) {
            draw(g, 0, 0, 0, width / 2, height / 2);
            draw(g, 1, width / 2, 0, width / 2, height / 2);
            draw(g, 2, 0, height / 2, width / 2, height / 2);
            draw(g, 3, width / 2, height / 2, width / 2, height / 2);
        } else if (len == 5) {
            draw(g, 0, 0, 0, width / 3, height / 2);
            draw(g, 1, width / 3, 0, width / 3, height / 2);
            draw(g, 2, (width * 2) / 3, 0, width / 3, height / 2);
            draw(g, 3, 0, height / 2, width / 2, height / 2);
            draw(g, 4, width / 2, height / 2, width / 2, height / 2);
        } else if (len == 6) {
            draw(g, 0, 0, 0, width / 3, height / 2);
            draw(g, 1, width / 3, 0, width / 3, height / 2);
            draw(g, 2, (width * 2) / 3, 0, width / 3, height / 2);
            draw(g, 3, 0, height / 2, width / 3, height / 2);
            draw(g, 4, width / 3, height / 2, width / 3, height / 2);
            draw(g, 5, (width * 2) / 3, height / 2, width / 3, height / 2);
        } else {
            return;
        }
        final Font FONT = new Font("Helvetica", 1, 13);
        FontMetrics metrics = g.getFontMetrics(FONT);
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, height - 30, width, 30);
        g.setColor(Color.white);
        g.drawString("Currently running " + (bots.length == 1 ? "1 bot." : bots.length + " bots."), 5, height + metrics.getDescent() - 14);
    }

    public void draw(Graphics g, int idx, int x, int y, int width, int height) {
        BotLiteInterface[] bots = Application.getBots();
        BufferedImage img = ((BotLite) bots[idx]).getBackBuffer();
        if (img != null && img.getWidth() > 0) {
            int w_img = img.getWidth(), h_img = img.getHeight();
            float img_ratio = (float) w_img / (float) h_img;
            float bound_ratio = (float) width / (float) height;
            int w, h;
            if (img_ratio < bound_ratio) {
                h = height;
                w = (int) (((float) w_img / (float) h_img) * h);
            } else {
                w = width;
                h = (int) (((float) h_img / (float) w_img) * w);
            }
            g.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x + width / 2 - w / 2, y + height / 2 - h / 2, null);
            g.setColor(Color.gray);
            g.drawRect(x, y, width - 1, height - 1);
        }
    }
}
