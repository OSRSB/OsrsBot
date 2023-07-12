package net.runelite.rsb.service;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.util.ReflectUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author GigiaJ
 */
class ScriptClassLoader extends URLClassLoader implements ReflectUtil.PrivateLookupableClassLoader{

	private final URL base;

	public ScriptClassLoader(URL url) {
		super(new URL[]{url}, ScriptClassLoader.class.getClassLoader());
		this.base = url;

		ReflectUtil.installLookupHelper(this);
	}

	@SuppressWarnings("rawtypes")
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class clazz = findLoadedClass(name);

		if (clazz == null) {
			try {
				InputStream in = getResourceAsStream(name.replace('.', '/') + ".class");
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
				clazz = super.loadClass(name, resolve);
			}
		}

		return clazz;
	}

	public URL getResource(String name) {
		try {
			return new URL(base, name);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public InputStream getResourceAsStream(String name) {
		try {
			return new URL(base, name).openStream();
		} catch (IOException e) {
			return null;
		}
	}


	@Getter
	@Setter
	private MethodHandles.Lookup lookup;

	@Override
	public Class<?> defineClass0(String name, byte[] b, int off, int len) throws ClassFormatError {
		return super.defineClass(name, b, off, len);
	}

}
