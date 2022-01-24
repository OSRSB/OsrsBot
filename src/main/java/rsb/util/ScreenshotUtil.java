package rsb.util;

import rsb.botLauncher.RuneLite;
import rsb.internal.globval.GlobalConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScreenshotUtil {

	private static final Logger log = Logger.getLogger(ScreenshotUtil.class.getName());
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

	public static void saveScreenshot(final RuneLite bot, final boolean hideUsername) {
		final String name = ScreenshotUtil.dateFormat.format(new Date()) + ".png";
		final File dir = new File(GlobalConfiguration.Paths.getScreenshotsDirectory());
		if (dir.isDirectory() || dir.mkdirs()) {
			ScreenshotUtil.saveScreenshot(bot, new File(dir, name), "png", hideUsername);
		}
	}

	private static void saveScreenshot(final RuneLite bot, final File file, final String type, final boolean hideUsername) {
		try {
			BufferedImage image = takeScreenshot(bot, hideUsername);

			ImageIO.write(image, type, file);
			ScreenshotUtil.log.info("Screenshot saved to: " + file.getPath());
		} catch (final Exception e) {
			ScreenshotUtil.log.log(Level.SEVERE, "Could not take screenshot.", e);
		}
	}

	public static BufferedImage takeScreenshot(final RuneLite bot, final boolean hideUsername) {
		final BufferedImage source = bot.getImage();
		final WritableRaster raster = source.copyData(null);

		final BufferedImage bufferedImage = new BufferedImage(source.getColorModel(), raster,
				source.isAlphaPremultiplied(), null);
		final Graphics2D graphics = bufferedImage.createGraphics();

		if (hideUsername) {
			if (bot.getMethodContext().gui.isFixed()) {
				graphics.setColor(Color.black);
				graphics.fill(new Rectangle(9, 459, 100, 15));
				graphics.dispose();
			} else {
				graphics.setColor(Color.black);
				graphics.drawRect(8, 555, 100, 15);
				graphics.dispose();
			}
		}
		return source;
	}

}
