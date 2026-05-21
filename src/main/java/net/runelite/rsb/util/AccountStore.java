package net.runelite.rsb.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author GigiaJ
 */
@Slf4j
public class AccountStore {

	public static class Account {

		private final String username;
		private String password;
		private final Map<String, String> attributes = new TreeMap<>();

		public Account(String username) {
			this.username = username;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			boolean safe = true;
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				safe = safe && (stackTraceElement.getClassName().contains("rsb") || stackTraceElement
						.getClassName().contains("java.lang.T") || stackTraceElement
						.getClassName().contains("java.awt.") || stackTraceElement
						.getClassName().contains("javax.swing.") || stackTraceElement
						.getClassName().contains("java.security.") || stackTraceElement
						.getClassName().contains("sun.awt.") || stackTraceElement.getClassName().contains("org.pushingpixels")
				);
			}
			return safe ? password : null;
		}

		public String getAttribute(String key) {
			boolean safe = true;
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			if (key.equalsIgnoreCase("pin")) {
				for (StackTraceElement stackTraceElement : stackTraceElements) {
					safe = safe && (stackTraceElement.getClassName().contains("rsb") || stackTraceElement
							.getClassName().contains("java.lang.T") || stackTraceElement
							.getClassName().contains("java.awt.") || stackTraceElement
							.getClassName().contains("javax.swing.") || stackTraceElement
							.getClassName().contains("java.security.") || stackTraceElement
							.getClassName().contains("sun.awt.") || stackTraceElement.getClassName().contains("org.pushingpixels"));
				}
			}
			return safe ? attributes.get(key) : null;
		}

		public void setAttribute(String key, String value) {
			attributes.put(key, value);
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String toString() {
			return "Account[" + username + "]";
		}

	}

	public static final String KEY_ALGORITHM = "DESede";
	public static final String CIPHER_TRANSFORMATION = "DESede/CBC/PKCS5Padding";
	public static final int FORMAT_VERSION = 2;

	private static final int DESEDE_KEY_LENGTH = 24;

	private final File file;
	private byte[] digest;
	private final String[] protectedAttributes = {"pin"};

	private final Map<String, Account> accounts = new TreeMap<>();

	public AccountStore(File file) {
		this.file = file;
	}

	/**
	 * Loads or generates a per-installation encryption key stored next to the accounts file.
	 * Returns the raw 24-byte DESede key, or null if key file operations fail.
	 */
	public static byte[] loadOrCreateInstallKey(File keyFile) {
		if (keyFile.exists()) {
			try {
				byte[] encoded = Files.readAllBytes(keyFile.toPath());
				byte[] key = java.util.Base64.getDecoder().decode(encoded);
				if (key.length == DESEDE_KEY_LENGTH) {
					return key;
				}
				log.warn("Install key file has unexpected length {}; regenerating", key.length);
			} catch (IOException e) {
				log.error("Failed to read install key file, regenerating", e);
			}
		}
		byte[] key = new byte[DESEDE_KEY_LENGTH];
		new SecureRandom().nextBytes(key);
		try {
			Files.write(keyFile.toPath(), java.util.Base64.getEncoder().encode(key));
			log.info("Generated new per-installation encryption key at {}", keyFile.getAbsolutePath());
		} catch (IOException e) {
			log.error("Failed to save install key file; account data will not be encrypted persistently", e);
		}
		return key;
	}

	public Account get(String username) {
		return accounts.get(username);
	}

	public void remove(String username) {
		accounts.remove(username);
	}

	public void add(Account account) {
		accounts.put(account.username, account);
	}

	public Collection<Account> list() {
		return accounts.values();
	}

	public void load() throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!file.canRead() || !file.canWrite()) {
			file.setReadable(true);
			file.setWritable(true);
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String versionLine = br.readLine();
			if (versionLine == null || versionLine.isBlank()) {
				return;
			}
			try {
				int v = Integer.parseInt(versionLine.trim());
				if (v != FORMAT_VERSION) {
					throw new IOException("unsupported format version: " + v);
				}
			} catch (NumberFormatException ex) {
				throw new IOException("bad format");
			}
			accounts.clear();
			Account current = null;
			for (; ;) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("[") && line.endsWith("]")) {
					if (current != null) {
						accounts.put(current.username, current);
					}
					String name = AccountStore.fixName(line.trim().substring(1).substring(0, line.length() - 2));
					current = new Account(name);
					continue;
				}
				if (current != null && line.matches("^\\w+=.+$")) {
					String[] split = line.trim().split("=", 2);
					if (split[0].equals("password")) {
						current.password = decrypt(split[1]);
					} else {
						if (Arrays.asList(protectedAttributes).contains(split[0])) {
							split[1] = decrypt(split[1]);
						}
						current.setAttribute(split[0], split[1]);
					}
				}
			}
			if (current != null) {
				accounts.put(current.username, current);
			}
		}
	}

	public void save() throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(Integer.toString(FORMAT_VERSION));
			bw.newLine();
			for (String name : accounts.keySet()) {
				bw.append("[").append(AccountStore.fixName(name.trim())).append("]");
				bw.newLine();
				String password = accounts.get(name).password;
				if (password != null) {
					bw.append("password=");
					bw.append(encrypt(password));
				}
				bw.newLine();
				for (Map.Entry<String, String> entry : accounts.get(name).attributes.entrySet()) {
					String key = entry.getKey(), value = entry.getValue();
					if (Arrays.asList(protectedAttributes).contains(key)) {
						value = encrypt(value);
					}
					bw.append(key).append("=").append(value);
					bw.newLine();
				}
			}
		}
	}

	/**
	 * Sets the encryption key from a raw 24-byte DESede key (e.g. from {@link #loadOrCreateInstallKey}).
	 */
	public void setKeyBytes(byte[] keyBytes) {
		if (keyBytes == null || keyBytes.length != DESEDE_KEY_LENGTH) {
			throw new IllegalArgumentException("Key must be exactly " + DESEDE_KEY_LENGTH + " bytes");
		}
		digest = Arrays.copyOf(keyBytes, DESEDE_KEY_LENGTH);
	}

	/**
	 * Sets the encryption key by hashing a password string (legacy path).
	 */
	public void setPassword(String password) {
		if (password == null) {
			digest = null;
			return;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(password.getBytes("iso-8859-1"), 0, password.length());
			digest = md.digest();
		} catch (Exception e) {
			throw new RuntimeException("Unable to digest password");
		}
		digest = Arrays.copyOf(digest, 24);
		for (int i = 0, off = 20; i < 4; ++i) {
			digest[off++] = digest[i];
		}
	}

	private String encrypt(String data) {
		if (digest == null) {
			byte[] enc = Base64.encodeBase64(StringUtil.getBytesUtf8(data));
			return StringUtil.newStringUtf8(enc);
		}
		SecretKey key = new SecretKeySpec(digest, KEY_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(new byte[8]);

		byte[] enc;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			enc = cipher.doFinal(StringUtil.getBytesUtf8(data));
		} catch (Exception e) {
			throw new RuntimeException("Unable to encrypt data!");
		}
		return StringUtil.newStringUtf8(Base64.encodeBase64(enc));
	}

	private String decrypt(String data) throws IOException {
		if (digest == null) {
			byte[] enc = Base64.decodeBase64(StringUtil.getBytesUtf8(data));
			return StringUtil.newStringUtf8(enc);
		}
		SecretKey key = new SecretKeySpec(digest, KEY_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(new byte[8]);

		byte[] dec;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			dec = cipher.doFinal(Base64.decodeBase64(data));
		} catch (Exception e) {
			throw new IOException("Unable to decrypt data!");
		}
		return StringUtil.newStringUtf8(dec);
	}

	/**
	 * Capitalizes the first character and replaces spaces with underscores.
	 * Purely aesthetic.
	 *
	 * @param name The name of the account
	 * @return Fixed name
	 */
	public static String fixName(String name) {
		if (name.contains("@")) {
			name = name.toLowerCase().trim();
		} else {
			if (name.charAt(0) > 91) {
				name = (char) (name.charAt(0) - 32) + name.substring(1);
			}
			name = name.replaceAll("\\s", "_");
		}
		return name;
	}

}

