package net.runelite.rsb.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import static org.junit.Assert.*;

public class AccountStoreTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    // -------------------------------------------------------------------------
    // fixName
    // -------------------------------------------------------------------------

    @Test
    public void fixNameCapitalizesLowercaseFirstLetter() {
        assertEquals("Bob", AccountStore.fixName("bob"));
    }

    @Test
    public void fixNameDoesNotChangeAlreadyCapitalized() {
        assertEquals("Bob", AccountStore.fixName("Bob"));
    }

    @Test
    public void fixNameReplacesSpacesWithUnderscores() {
        assertEquals("Bob_smith", AccountStore.fixName("bob smith"));
    }

    @Test
    public void fixNameHandlesMultipleSpaces() {
        assertEquals("Bob_the_builder", AccountStore.fixName("bob the builder"));
    }

    @Test
    public void fixNameEmailLowercasesAndTrims() {
        assertEquals("user@example.com", AccountStore.fixName("User@Example.Com"));
    }

    @Test
    public void fixNameEmailPreservesAlreadyLowercase() {
        assertEquals("user@example.com", AccountStore.fixName("user@example.com"));
    }

    // -------------------------------------------------------------------------
    // loadOrCreateInstallKey
    // -------------------------------------------------------------------------

    @Test
    public void loadOrCreateInstallKeyGenerates24ByteKey() throws IOException {
        File keyFile = tmp.newFile("test.key");
        keyFile.delete(); // start fresh
        byte[] key = AccountStore.loadOrCreateInstallKey(keyFile);
        assertNotNull(key);
        assertEquals(24, key.length);
    }

    @Test
    public void loadOrCreateInstallKeyPersistsKeyFile() throws IOException {
        File keyFile = tmp.newFile("test.key");
        keyFile.delete();
        AccountStore.loadOrCreateInstallKey(keyFile);
        assertTrue(keyFile.exists());
    }

    @Test
    public void loadOrCreateInstallKeyLoadsExistingKeyConsistently() throws IOException {
        File keyFile = tmp.newFile("test.key");
        keyFile.delete();
        byte[] key1 = AccountStore.loadOrCreateInstallKey(keyFile);
        byte[] key2 = AccountStore.loadOrCreateInstallKey(keyFile);
        assertArrayEquals(key1, key2);
    }

    @Test
    public void loadOrCreateInstallKeyRegeneratesCorruptedFile() throws IOException {
        File keyFile = tmp.newFile("bad.key");
        java.nio.file.Files.write(keyFile.toPath(), "not-valid-base64!!!".getBytes());
        byte[] key = AccountStore.loadOrCreateInstallKey(keyFile);
        assertNotNull(key);
        assertEquals(24, key.length);
    }

    // -------------------------------------------------------------------------
    // setKeyBytes validation
    // -------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void setKeyBytesRejectsNull() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        store.setKeyBytes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKeyBytesRejectsTooShortKey() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        store.setKeyBytes(new byte[16]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKeyBytesRejectsTooLongKey() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        store.setKeyBytes(new byte[32]);
    }

    @Test
    public void setKeyBytesAcceptsExactly24Bytes() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        byte[] key = new byte[24];
        new SecureRandom().nextBytes(key);
        store.setKeyBytes(key); // must not throw
    }

    // -------------------------------------------------------------------------
    // Account CRUD
    // -------------------------------------------------------------------------

    @Test
    public void addAndGetAccount() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        AccountStore.Account acc = new AccountStore.Account("alice");
        store.add(acc);
        assertSame(acc, store.get("alice"));
    }

    @Test
    public void getReturnsNullForUnknownUsername() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        assertNull(store.get("nobody"));
    }

    @Test
    public void removeDeletesAccount() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        store.add(new AccountStore.Account("alice"));
        store.remove("alice");
        assertNull(store.get("alice"));
    }

    @Test
    public void listReturnsAllAddedAccounts() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        store.add(new AccountStore.Account("alice"));
        store.add(new AccountStore.Account("bob"));
        store.add(new AccountStore.Account("charlie"));
        assertEquals(3, store.list().size());
    }

    @Test
    public void listIsEmptyOnFreshStore() throws IOException {
        AccountStore store = new AccountStore(tmp.newFile("accounts.ini"));
        assertTrue(store.list().isEmpty());
    }

    // -------------------------------------------------------------------------
    // Account — username & non-protected attributes
    // -------------------------------------------------------------------------

    @Test
    public void accountGetUsername() {
        assertEquals("myuser", new AccountStore.Account("myuser").getUsername());
    }

    @Test
    public void accountToString() {
        assertEquals("Account[myuser]", new AccountStore.Account("myuser").toString());
    }

    @Test
    public void accountSetAndGetNonProtectedAttribute() {
        AccountStore.Account acc = new AccountStore.Account("user");
        acc.setAttribute("reward", "Gems");
        assertEquals("Gems", acc.getAttribute("reward"));
    }

    @Test
    public void accountGetAttributeReturnsNullForMissingKey() {
        assertNull(new AccountStore.Account("user").getAttribute("nonexistent"));
    }

    @Test
    public void accountAttributeOverwrite() {
        AccountStore.Account acc = new AccountStore.Account("user");
        acc.setAttribute("reward", "Cash");
        acc.setAttribute("reward", "Gems");
        assertEquals("Gems", acc.getAttribute("reward"));
    }

    // -------------------------------------------------------------------------
    // load() on edge-case files
    // -------------------------------------------------------------------------

    @Test
    public void loadOnEmptyFileProducesNoAccounts() throws IOException {
        File f = tmp.newFile("empty.ini");
        AccountStore store = new AccountStore(f);
        store.setKeyBytes(randomKey());
        store.load(); // must not throw
        assertEquals(0, store.list().size());
    }

    @Test
    public void loadCreatesFileIfMissing() throws IOException {
        File f = tmp.newFile("missing.ini");
        f.delete();
        assertFalse(f.exists());
        AccountStore store = new AccountStore(f);
        store.setKeyBytes(randomKey());
        store.load();
        assertTrue(f.exists());
    }

    // -------------------------------------------------------------------------
    // save() / load() round-trip
    // -------------------------------------------------------------------------

    @Test
    public void roundTripPreservesAccountCount() throws IOException {
        File f = tmp.newFile("accounts.ini");
        byte[] key = randomKey();

        AccountStore storeA = storeWithKey(f, key);
        storeA.add(new AccountStore.Account("alice"));
        storeA.add(new AccountStore.Account("bob"));
        storeA.add(new AccountStore.Account("charlie"));
        storeA.save();

        AccountStore storeB = storeWithKey(f, key);
        storeB.load();
        assertEquals(3, storeB.list().size());
    }

    @Test
    public void roundTripPreservesNonProtectedAttributes() throws IOException {
        File f = tmp.newFile("accounts.ini");
        byte[] key = randomKey();

        AccountStore storeA = storeWithKey(f, key);
        AccountStore.Account acc = new AccountStore.Account("alice");
        acc.setAttribute("reward", "Gems");
        acc.setAttribute("member", "true");
        storeA.add(acc);
        storeA.save();

        AccountStore storeB = storeWithKey(f, key);
        storeB.load();

        // fixName("alice") → "Alice"
        AccountStore.Account loaded = storeB.get("Alice");
        assertNotNull(loaded);
        assertEquals("Gems", loaded.getAttribute("reward"));
        assertEquals("true", loaded.getAttribute("member"));
    }

    @Test
    public void roundTripWithPasswordEncryptedOnDisk() throws IOException {
        File f = tmp.newFile("accounts.ini");
        byte[] key = randomKey();

        AccountStore storeA = storeWithKey(f, key);
        AccountStore.Account acc = new AccountStore.Account("testuser");
        acc.setPassword("supersecret");
        storeA.add(acc);
        storeA.save();

        // Verify raw file does NOT contain plaintext password
        String rawContents = new String(java.nio.file.Files.readAllBytes(f.toPath()));
        assertFalse("Password should not appear in plaintext", rawContents.contains("supersecret"));

        // Account survives round-trip
        AccountStore storeB = storeWithKey(f, key);
        storeB.load();
        assertNotNull(storeB.get("Testuser")); // fixName capitalises 't'
    }

    @Test
    public void roundTripWithMultipleAttributesPerAccount() throws IOException {
        File f = tmp.newFile("accounts.ini");
        byte[] key = randomKey();

        AccountStore storeA = storeWithKey(f, key);
        AccountStore.Account acc = new AccountStore.Account("player");
        acc.setAttribute("reward", "Cash");
        acc.setAttribute("member", "false");
        acc.setAttribute("take_breaks", "true");
        storeA.add(acc);
        storeA.save();

        AccountStore storeB = storeWithKey(f, key);
        storeB.load();
        AccountStore.Account loaded = storeB.get("Player");
        assertNotNull(loaded);
        assertEquals("Cash", loaded.getAttribute("reward"));
        assertEquals("false", loaded.getAttribute("member"));
        assertEquals("true", loaded.getAttribute("take_breaks"));
    }

    @Test
    public void differentKeysCannotDecryptEachOthersData() throws IOException {
        File f = tmp.newFile("accounts.ini");
        byte[] keyA = randomKey();
        byte[] keyB = randomKey();
        // Ensure keys are different
        keyB[0] ^= 0xFF;

        AccountStore storeA = storeWithKey(f, keyA);
        AccountStore.Account acc = new AccountStore.Account("user");
        acc.setAttribute("reward", "Cash");
        storeA.add(acc);
        storeA.save();

        // Loading with a different key should either fail or produce garbage,
        // but must not crash the JVM — any exception is acceptable
        AccountStore storeB = storeWithKey(f, keyB);
        try {
            storeB.load();
            // If it loaded without error the attribute value will be garbled — not "Cash"
            AccountStore.Account loaded = storeB.get("User");
            if (loaded != null) {
                assertNotEquals("Cash", loaded.getAttribute("reward"));
            }
        } catch (IOException expected) {
            // decrypt failure surfaced as IOException — acceptable
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static byte[] randomKey() {
        byte[] key = new byte[24];
        new SecureRandom().nextBytes(key);
        return key;
    }

    private static AccountStore storeWithKey(File f, byte[] key) {
        AccountStore store = new AccountStore(f);
        store.setKeyBytes(key);
        return store;
    }
}
