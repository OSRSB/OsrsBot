package net.runelite.rsb.util;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.internal.globval.GlobalConfiguration;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

@Slf4j
public class Extractor implements Runnable {
	private static void saveTo(InputStream in, String outPath) {
		try (InputStream is = in; OutputStream out = new FileOutputStream(new File(outPath))) {
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			log.debug("Failed to save to {}", outPath, e);
		}
	}

	private final String[] args;

	public Extractor(String[] args) {
		this.args = args;
	}

	public void run() {
		ClassLoader loader = getClass().getClassLoader();
		String root = GlobalConfiguration.Paths.Resources.ROOT + "/";
		if (GlobalConfiguration.RUNNING_FROM_JAR) {
			try {
				if (GlobalConfiguration.getCurrentOperatingSystem() == GlobalConfiguration.OperatingSystem.WINDOWS) {
					Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_SCRIPTS_BAT), GlobalConfiguration.Paths.getOsrsBotDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_SCRIPTS_BAT);
					Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_FIND_JDK), GlobalConfiguration.Paths.getOsrsBotDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_FIND_JDK);
				} else {
					Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_SCRIPTS_SH), GlobalConfiguration.Paths.getOsrsBotDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_SCRIPTS_SH);
				}
				URL version = GlobalConfiguration.class.getClassLoader().getResource(GlobalConfiguration.Paths.Resources.VERSION);
				String p = version.toString().replace("jar:file:", "").replace("!/" + GlobalConfiguration.Paths.Resources.VERSION, "");
				try {
					p = URLDecoder.decode(p, "UTF-8");
				} catch (final UnsupportedEncodingException ignored) {
					log.debug("Extractor run encoding issue", ignored);
				}
				try (JarFile jar = new JarFile(new File(p))) {
					File out = new File(GlobalConfiguration.Paths.getScriptsExtractedCache());
					FileOutputStream fos = null;
					JarOutputStream jos = null;
					try {
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry e = entries.nextElement();
							if (e.getName().startsWith("scripts/")) {
								if (fos == null) {
									fos = new FileOutputStream(out);
									jos = new JarOutputStream(fos);
								}
								try (InputStream in = loader.getResourceAsStream(e.getName())) {
									jos.putNextEntry(new JarEntry(e.getName().substring(8)));
									byte[] buffer = new byte[256];
									int nRead;
									while ((nRead = in.read(buffer, 0, buffer.length)) >= 0) {
										jos.write(buffer, 0, nRead);
									}
								}
							}
						}
					} finally {
						if (jos != null) {
							jos.close();
						} else if (fos != null) {
							fos.close();
						}
					}
				}
			} catch (Exception e) {
				log.error("Failed to extract scripts from JAR", e);
			}
		} else if (args.length > 2) {
			if (args[0].toLowerCase().startsWith("delete")) {
				File jarOld = new File(args[1]);
				if (jarOld.exists())
					if (!jarOld.delete())
						jarOld.deleteOnExit();
				clearDirectory(new File(GlobalConfiguration.Paths.getCacheDirectory()), false);
			}
		}
	}

	public void clearDirectory(File path, final boolean deleteParent) {
		if (!path.exists())
			return;
		for (File file : path.listFiles()) {
			if (file.isDirectory())
				clearDirectory(file, true);
			else
				file.delete();
		}
		if (deleteParent)
			path.delete();
	}
}