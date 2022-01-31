package rsb.internal.globval;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class GlobalConfiguration {

	public enum OperatingSystem {
		MAC, WINDOWS, LINUX, UNKNOWN
	}

	public static class Paths {

		public static class Resources {
			public static final String ROOT = "../../../rsb";
			public static final String ROOT_IMG = ROOT ;//+ "/images";
			public static final String ICON = GlobalConfiguration.class.getResource("rsb/plugin/rsb.png").getPath();
			public static final String VERSION = ROOT + "/version.txt";
		}

		public static class URLs {
			private static final String BASE = "https://github.com/OSRSB/";
			public static final String DOWNLOAD = BASE + "releases";
			public static final String UPDATE = BASE + "modscript";
			public static final String WEB = BASE + "webwalker.gz";
			public static final String PROJECT = BASE + "RSB";
			public static final String VERSION = BASE + PROJECT +"/version.txt";
			public static final String SITE = BASE + "RSBSite-PlaceHolder-";
			public static final String SDN_CONTROL = BASE + "sdn-control";
			public static final String AD_INFO = BASE + "botad-info";
		}

		public static final String COMPILE_SCRIPTS_BAT = "Compile-Scripts.bat";
		public static final String COMPILE_SCRIPTS_SH = "compile-scripts.sh";
		public static final String COMPILE_FIND_JDK = "FindJDK.bat";

		public static final String SCRIPTS_NAME_SRC = "scripts";
		public static final String SCRIPTS_NAME_OUT = "Scripts";

		public static String getAccountsFile() {
			final String path;
			if (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS) {
				path = System.getenv("APPDATA") + File.separator
						+ GlobalConfiguration.NAME + "_Accounts.ini";
			} else {
				path = Paths.getUnixHome() + File.separator + "."
						+ GlobalConfiguration.NAME_LOWERCASE + "acct";
			}
			return path;
		}

		public static String getHomeDirectory() {
			final String env = System.getenv(GlobalConfiguration.NAME.toUpperCase() + "_HOME");
			if ((env == null) || env.isEmpty()) {
				String test = GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS ?
						System.getProperty("user.home") :
						Paths.getUnixHome();
				return (test + File.separator + GlobalConfiguration.NAME);
			} else {
				return env;
			}
		}

		public static String getLogsDirectory() {
			return Paths.getHomeDirectory() + File.separator + "Logs";
		}

		public static String getMenuCache() {
			return Paths.getSettingsDirectory() + File.separator + "Menu.txt";
		}

		public static String getPathCache() {
			return Paths.getSettingsDirectory() + File.separator + "path.txt";
		}

		public static String getBootCache() {
			return Paths.getSettingsDirectory() + File.separator + "boot.txt";
		}

		public static String getUIDsFile() {
			return Paths.getSettingsDirectory() + File.separator + "uid.txt";
		}

		public static String getScreenshotsDirectory() {
			return Paths.getHomeDirectory() + File.separator + "Screenshots";
		}

		public static String getScriptsDirectory() {
			return Paths.getHomeDirectory() + File.separator + Paths.SCRIPTS_NAME_OUT;
		}
		

		public static String getScriptsSourcesDirectory() {
			return Paths.getScriptsDirectory() + File.separator + "Sources";
		}

		public static String getScriptsPrecompiledDirectory() {
			return Paths.getScriptsDirectory() + File.separator + "Precompiled";
		}

		public static String getScriptsNetworkDirectory() {
			return Paths.getScriptsDirectory() + File.separator + "Network";
		}

		public static String getCacheDirectory() {
			return Paths.getHomeDirectory() + File.separator + "Cache";
		}

		public static String getScriptsExtractedCache() {
			return	Paths.getCacheDirectory() + File.separator + "script.dat";
		}

		public static String getVersionCache() {
			return Paths.getCacheDirectory() + File.separator + "info.dat";
		}

		public static String getModScriptCache() {
			return Paths.getCacheDirectory() + File.separator + "ms.dat";
		}

		public static String getClientCache() {
			return Paths.getCacheDirectory() + File.separator + "client.dat";
		}

		public static String getWebCache() {
			return Paths.getCacheDirectory() + File.separator + "web.dat";
		}

		public static String getHackCache() {
			return Paths.getCacheDirectory() + File.separator + "hack.dat";
		}

		public static String getSettingsDirectory() {
			return Paths.getHomeDirectory() + File.separator + "Settings";
		}

		public static String getMenuBarPrefs() {
			return Paths.getSettingsDirectory() + File.separator + "Menu.txt";
		}

		public static String getUnixHome() {
			final String home = System.getProperty("user.home");
			return home == null ? "~" : home;
		}
	}

	public static final String NAME = "GService";
	public static final String NAME_LOWERCASE = NAME.toLowerCase();
	private static final OperatingSystem CURRENT_OS;
	public static boolean RUNNING_FROM_JAR = false;
	public static final boolean SCRIPT_DRM = true;
	
	
	public static class Twitter {
		public static final boolean ENABLED = true;
		public static final String NAME = "rsbotorg";
		public static final String HASHTAG = "#" + NAME_LOWERCASE;
		public static final int MESSAGES = 3;
	}

	/**
	 * When executed it starts up the general configurations and paths as well as determines what files will be auto-generated
	 */
	static {
		final URL resource = GlobalConfiguration.class.getProtectionDomain().getCodeSource().getLocation();
		if (resource.toString().contains("jar")) {
			GlobalConfiguration.RUNNING_FROM_JAR = true;
		}
		final String os = System.getProperty("os.name");
		if (os.contains("Mac")) {
			CURRENT_OS = OperatingSystem.MAC;
		} else if (os.contains("Windows")) {
			CURRENT_OS = OperatingSystem.WINDOWS;
		} else if (os.contains("Linux")) {
			CURRENT_OS = OperatingSystem.LINUX;
		} else {
			CURRENT_OS = OperatingSystem.UNKNOWN;
		}
		final ArrayList<String> dirs = new ArrayList<String>();
		//This is where folders and files are generated on start-up
		dirs.add(Paths.getHomeDirectory());
		dirs.add(Paths.getLogsDirectory());
		dirs.add(Paths.getCacheDirectory());
		dirs.add(Paths.getSettingsDirectory());
		dirs.add(Paths.getScriptsDirectory());
		dirs.add(Paths.getScriptsSourcesDirectory());
		dirs.add(Paths.getScriptsPrecompiledDirectory());


		if (GlobalConfiguration.RUNNING_FROM_JAR) {
			dirs.add(Paths.getScriptsDirectory());
			dirs.add(Paths.getScriptsSourcesDirectory());
			dirs.add(Paths.getScriptsPrecompiledDirectory());
		}


		for (final String name : dirs) {
			final File dir = new File(name);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		final ByteArrayOutputStream logout = new ByteArrayOutputStream();
		if (GlobalConfiguration.RUNNING_FROM_JAR) {
			String path = resource.toString();
			try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (final UnsupportedEncodingException ignored) {
			}
			final String prefix = "jar:file:/";
			if (path.indexOf(prefix) == 0) {
				path = path.substring(prefix.length());
				path = path.substring(0, path.indexOf('!'));
				if (File.separatorChar != '/') {
					path = path.replace('/', File.separatorChar);
				}
				try {
					final File pathfile = new File(Paths.getPathCache());
					if (pathfile.exists()) {
						pathfile.delete();
					}
					pathfile.createNewFile();
					Writer out = new BufferedWriter(new FileWriter(Paths.getPathCache()));
					out.write(path);
					out.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static URL getResourceURL(final String path) throws MalformedURLException {
		return new File(path).toURI().toURL();
	}

	public static Image getImage(String resource) {
		try {
			return Toolkit.getDefaultToolkit().getImage(getResourceURL(resource));
		} catch (Exception e) { }
		return null;
	}

	public static OperatingSystem getCurrentOperatingSystem() {
		return GlobalConfiguration.CURRENT_OS;
	}
	
	static String httpUserAgent = null;

	public static String getHttpUserAgent() {
		if (httpUserAgent != null)
			return httpUserAgent;
		String os = "Windows NT 6.1";
		if (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.MAC)
			os = "Macintosh; Intel Mac OS X 10_6_6";
		else if (GlobalConfiguration.getCurrentOperatingSystem() != OperatingSystem.WINDOWS)
			os = "X11; Linux x86_64";
		StringBuilder buf = new StringBuilder(125);
		buf.append("Mozilla/5.0 (").append(os).append(")");
		buf.append(" AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.60 Safari/534.24");
		httpUserAgent = buf.toString();
		return httpUserAgent;
	}

	public static HttpURLConnection getHttpConnection(final URL url) throws IOException {
		final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		con.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		con.addRequestProperty("Accept-Encoding", "gzip,deflate");
		con.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
		con.addRequestProperty("Host", url.getHost());
		con.addRequestProperty("User-Agent", getHttpUserAgent());
		con.setConnectTimeout(10000);
		return con;
	}

	public static int getVersion() {
		InputStreamReader is = null;
		BufferedReader reader = null;
		try {
			is = new InputStreamReader(RUNNING_FROM_JAR ?
					GlobalConfiguration.class.getClassLoader().getResourceAsStream(
							Paths.Resources.VERSION) : new FileInputStream(Paths.Resources.VERSION));
			reader = new BufferedReader(is);
			String s = reader.readLine().trim();
			return Integer.parseInt(s);
		} catch (Exception e) {
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ioe) {
			}
		}
		return -1;
	}

	public static String getVersionFormatted() {
		return getVersionFormatted(getVersion());
	}

	public static String getVersionFormatted(final int version) {
		final float v = (float) version / 100;
		String s = Float.toString(v);
		final int z = s.indexOf('.');
		if (z == -1) {
			s += ".00";
		} else {
			final String exp = s.substring(z + 1);
			if (exp.length() == 1) {
				s += "0";
			}
		}
		return s;
	}
}
