package rsb.util;

import lombok.extern.slf4j.Slf4j;
import rsb.internal.globval.GlobalConfiguration;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StringUtil {

	private static final String[] COLOURS_STR = new String[]{"red", "green", "cyan", "purple", "white"};
	private static final Map<String, Color> COLOR_MAP = new HashMap<String, Color>();

	public static String join(final String[] s) {
		final int l = s.length;
		switch (l) {
			case 0:
				return "";
			case 1:
				return s[0];
		}
		final String d = ", ";
		final int x = d.length();
		int n = 0, i;
		for (i = 0; i < l; i++) {
			n += s[i].length() + x;
		}
		final StringBuffer buf = new StringBuffer(n - x);
		i = 0;
		boolean c = true;
		while (c) {
			buf.append(s[i]);
			i++;
			c = i < l;
			if (c) {
				buf.append(d);
			}
		}
		return buf.toString();
	}

	/**
	 * Draws a line on the screen at the specified index. Default is green.
	 *
	 * Available colours: red, green, cyan, purple, white.
	 *
	 * @param render The Graphics object to be used.
	 * @param row    The index where you want the text.
	 * @param text   The text you want to render. Colours can be set like [red].
	 */
	public static void drawLine(Graphics render, int row, String text) {
		FontMetrics metrics = render.getFontMetrics();
		int height = metrics.getHeight() + 4; // height + gap
		int y = row * height + 15 + 19;
		String[] texts = text.split("\\[");
		int xIdx = 7;
		Color cur = Color.GREEN;
		for (String t : texts) {
			for (@SuppressWarnings("unused") String element : COLOURS_STR) {
				// String element = COLOURS_STR[i];
				// Don't search for a starting '[' cause it they don't exists.
				// we split on that.
				int endIdx = t.indexOf(']');
				if (endIdx != -1) {
					String colorName = t.substring(0, endIdx);
					if (COLOR_MAP.containsKey(colorName)) {
						cur = COLOR_MAP.get(colorName);
					} else {
						try {
							Field f = Color.class.getField(colorName);
							int mods = f.getModifiers();
							if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods)) {
								cur = (Color) f.get(null);
								COLOR_MAP.put(colorName, cur);
							}
						} catch (Exception ignored) {
							log.debug("Error with drawing line", ignored);
						}
					}
					t = t.replace(colorName + "]", "");
				}
			}
			render.setColor(Color.BLACK);
			render.drawString(t, xIdx, y + 1);
			render.setColor(cur);
			render.drawString(t, xIdx, y);
			xIdx += metrics.stringWidth(t);
		}
	}

	public static String throwableToString(Throwable t) {
		if (t != null) {
			Writer exception = new StringWriter();
			PrintWriter printWriter = new PrintWriter(exception);
			t.printStackTrace(printWriter);
			return exception.toString();
		}
		return "";
	}

	public static byte[] getBytesUtf8(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String newStringUtf8(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Gets the compiled path of the class containing the object
	 * and replaces the path with the values needed to direct it to the
	 * non-compiled path with the .java file to access
	 * Used only in IntelliJ due to the weird behavior when building w/ the run-time compiler for test scripts
	 * TODO: Remove this via Gradle behavior such as a resources folder
	 *
	 * @param clazz 	The class of the object in the same directory as the one we wish to find
	 * @param file		The file we wish to locate within the above directory
	 *
	 * @return 			The path of the item in IntelliJ's non-build path
	 */
	public static String convertIntelliJPath(Class<?> clazz, String file) {
		String path = clazz.getResource(file).getPath().replace(file, "");
		if (!GlobalConfiguration.RUNNING_FROM_JAR) {
			path = path.replace("build/classes/java/main", "src/main/java");
		}
		return path;
	}
}
