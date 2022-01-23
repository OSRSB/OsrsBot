package rsb.service;

import lombok.extern.slf4j.Slf4j;
import rsb.script.Script;
import rsb.script.ScriptManifest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * @author GigiaJ
 */
@Slf4j
public class FileScriptSource implements ScriptSource {

	//private final Logger log = Logger.getLogger(getClass().getSimpleName());

	private File file;

	public FileScriptSource(File file) {
		this.file = file;
	}

	public List<ScriptDefinition> list() {
		LinkedList<ScriptDefinition> defs = new LinkedList<ScriptDefinition>();
		if (file != null) {
			if (file.isDirectory()) {
				try {
					ClassLoader ldr = new ScriptClassLoader(file.toURI().toURL());
					for (File f : file.listFiles()) {
						if (isJar(f)) {
							load(new ScriptClassLoader(getJarUrl(f)), defs, new JarFile(f));
						} else {
							load(ldr, defs, f, "");
						}
					}
				} catch (IOException ignored) {
					log.debug("Failed to list files", ignored);
				}
			} else if (isJar(file)) {
				try {
					ClassLoader ldr = new ScriptClassLoader(getJarUrl(file));
					load(ldr, defs, new JarFile(file));
				} catch (IOException ignored) {
					log.debug("Failed to list files", ignored);
				}
			}
		}
		return defs;
	}


	public Script load(ScriptDefinition def) throws ServiceException {
		if (!(def instanceof FileScriptDefinition)) {
			throw new IllegalArgumentException("Invalid definition!");
		}
		FileScriptDefinition fsd = (FileScriptDefinition) def;
		try {
			return fsd.clazz.asSubclass(Script.class).newInstance();
		} catch (Exception ex) {
			throw new ServiceException(ex.toString());
		}
	}

	private void load(ClassLoader loader, LinkedList<ScriptDefinition> scripts, JarFile jar) {
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry e = entries.nextElement();
			String name = e.getName().replace('/', '.');
			String ext = ".class";
			if (name.endsWith(ext) && !name.contains("$")) {
				load(loader, scripts, name.substring(0, name.length() - ext.length()));
			}
		}
	}

	private void load(ClassLoader loader, LinkedList<ScriptDefinition> scripts, File file, String prefix) {
		if (file.isDirectory()) {
			if (!file.getName().startsWith(".")) {
				for (File f : file.listFiles()) {
					load(loader, scripts, f, prefix + file.getName() + ".");
				}
			}
		} else {
			String name = prefix + file.getName();
			String ext = ".class";
			if (name.endsWith(ext) && !name.startsWith(".") && !name.contains("!") && !name.contains("$")) {
				name = name.substring(0, name.length() - ext.length());
				load(loader, scripts, name);
			}
		}
	}

	private void load(ClassLoader loader, LinkedList<ScriptDefinition> scripts, String name) {
		Class<?> clazz;
		try {
			clazz = loader.loadClass(name);
		} catch (Exception e) {
			log.warn(name + " is not a valid script and was ignored!", e);
			return;
		} catch (VerifyError e) {
			log.warn(name + " is not a valid script and was ignored!", e);
			return;
		}
		if (clazz.isAnnotationPresent(ScriptManifest.class)) {
			FileScriptDefinition def = new FileScriptDefinition();
			ScriptManifest manifest = clazz.getAnnotation(ScriptManifest.class);
			def.id = 0;
			def.name = manifest.name();
			def.authors = manifest.authors();
			def.version = manifest.version();
			def.keywords = manifest.keywords();
			def.description = manifest.description();
			def.website = manifest.website();
			def.clazz = clazz;
			def.source = this;
			scripts.add(def);
		}
	}

	private boolean isJar(File file) {
		return file.getName().endsWith(".jar") || file.getName().endsWith(".dat");
	}

	private URL getJarUrl(File file) throws IOException {
		URL url = file.toURI().toURL();
		url = new URL("jar:" + url.toExternalForm() + "!/");
		return url;
	}

	private static class FileScriptDefinition extends ScriptDefinition {

		Class<?> clazz;

	}

	public String toString() {
		return this.file.getAbsolutePath();
	}

}
