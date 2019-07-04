package net.runelite.client.rsb.gui;

import net.runelite.client.rsb.botLauncher.RuneLite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * @author Jacmob
 */
public class BotHome {

	private static final Font FONT = new Font("Helvetica", 1, 13);
	private int width;
	private int height;
	private RuneLite[] bots = new RuneLite[0];

	public void setBots(final Collection<RuneLite> col) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				bots = col.toArray(new RuneLite[col.size()]);
			}
		});
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// TODO: Replace; temporary preview.
	public void paint(Graphics g) {
		g.setColor(Color.black);
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
		FontMetrics metrics = g.getFontMetrics(FONT);
		g.setColor(new Color(0, 0, 0, 170));
		g.fillRect(0, height - 30, width, 30);
		g.setColor(Color.white);
		g.drawString("Spectating " + (bots.length == 1 ? "1 bot." : bots.length + " bots."), 5, height + metrics.getDescent() - 14);
	}

	public void draw(Graphics g, int idx, int x, int y, int width, int height) {
		BufferedImage img = bots[idx].getImage();
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