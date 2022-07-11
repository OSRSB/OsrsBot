package rsb.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author GigiaJ
 */
class ScriptClassLoader extends ClassLoader {

	private final URL base;

	public ScriptClassLoader(URL url) {
		this.base = url;
	}

	@SuppressWarnings("rawtypes")
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class clazz = findLoadedClass(name);

		if (clazz == null) {
			try {
				System.out.println(name);
				InputStream in = getResourceAsStream(name.replace('.', '/') + ".class");
				if (in == null) {
					System.out.println(name);
				}
				byte[] buffer = new byte[4096];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int n;
				while ((n = in.read(buffer, 0, 4096)) != -1) {
					out.write(buffer, 0, n);
				}
				byte[] bytes = out.toByteArray();
				clazz = defineClass(name, bytes, 0, bytes.length);
				if (resolve) {
					resolveClass(clazz);
				}
			} catch (Exception e) {
				try {
					e.printStackTrace();
					clazz = super.loadClass(name, resolve);
				} catch (Exception d) {
					clazz = ClassLoader.getPlatformClassLoader().loadClass(name);
				}
			}
		}

		return clazz;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		List<URL> allRes = new LinkedList<>();

		// load resource from this classloader
		Enumeration<URL> thisRes = findResources(name);
		if (thisRes != null) {
			while (thisRes.hasMoreElements()) {
				allRes.add(thisRes.nextElement());
			}
		}

		// then try finding resources from parent classloaders
		Enumeration<URL> parentRes = super.findResources(name);
		if (parentRes != null) {
			while (parentRes.hasMoreElements()) {
				allRes.add(parentRes.nextElement());
			}
		}

		// then find from system classloader
		Enumeration<URL> sysRes = ClassLoader.getSystemResources(name);
		if (sysRes != null) {
			while (sysRes.hasMoreElements()) {
				allRes.add(sysRes.nextElement());
			}
		}

		return new Enumeration<URL>() {
			Iterator<URL> it = allRes.iterator();

			@Override
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			@Override
			public URL nextElement() {
				return it.next();
			}
		};
	}

	@Override
	public URL getResource(String name) {
		URL res = findResource(name);
		if (res == null) {
			res = super.findResource(name);
		}
		if (res == null) {
			res = ClassLoader.getSystemResource(name);
		}
		return res;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		Objects.requireNonNull(name);
		URL url = getResource(name);
		if (url == null) {
			System.out.println(name + " is NULL. Please fix this.");
		}
		try {
			return url != null ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}

}
