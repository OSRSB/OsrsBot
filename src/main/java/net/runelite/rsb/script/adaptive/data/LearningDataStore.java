package net.runelite.rsb.script.adaptive.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.internal.globval.GlobalConfiguration;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * JSON persistence for learning data via Gson.
 * Stores data at {OsrsBotDir}/learning/{scriptName}/{accountName}/
 */
@Slf4j
public class LearningDataStore {
    private static final String LEARNING_DIR = "learning";
    private static final String STRATEGIES_FILE = "strategies.json";
    private static final String SESSIONS_FILE = "sessions.json";
    private static final int MAX_SESSIONS = 100;

    private final Gson gson;
    private final File dataDir;

    public LearningDataStore(String scriptName, String accountName) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        String safeName = sanitizeFileName(scriptName);
        String safeAccount = sanitizeFileName(accountName != null ? accountName : "default");

        this.dataDir = new File(GlobalConfiguration.Paths.getOsrsBotDirectory()
                + File.separator + LEARNING_DIR
                + File.separator + safeName
                + File.separator + safeAccount);
    }

    /**
     * Loads persisted strategy profiles.
     */
    public Map<String, StrategyProfile> loadStrategies() {
        File file = new File(dataDir, STRATEGIES_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, StrategyProfile>>() {}.getType();
            Map<String, StrategyProfile> result = gson.fromJson(reader, type);
            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            log.warn("Failed to load strategies from {}: {}", file.getPath(), e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Saves strategy profiles to disk.
     */
    public void saveStrategies(Map<String, StrategyProfile> strategies) {
        ensureDirectory();
        File file = new File(dataDir, STRATEGIES_FILE);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(strategies, writer);
        } catch (Exception e) {
            log.warn("Failed to save strategies to {}: {}", file.getPath(), e.getMessage());
        }
    }

    /**
     * Loads persisted session summaries.
     */
    public List<SessionSummary> loadSessions() {
        File file = new File(dataDir, SESSIONS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<List<SessionSummary>>() {}.getType();
            List<SessionSummary> result = gson.fromJson(reader, type);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.warn("Failed to load sessions from {}: {}", file.getPath(), e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Appends a session summary, keeping only the last MAX_SESSIONS entries.
     */
    public void saveSession(SessionSummary session) {
        List<SessionSummary> sessions = loadSessions();
        sessions.add(session);
        // Trim to max size
        while (sessions.size() > MAX_SESSIONS) {
            sessions.remove(0);
        }
        ensureDirectory();
        File file = new File(dataDir, SESSIONS_FILE);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(sessions, writer);
        } catch (Exception e) {
            log.warn("Failed to save sessions to {}: {}", file.getPath(), e.getMessage());
        }
    }

    /**
     * Returns the data directory path (for logging/debugging).
     */
    public String getDataPath() {
        return dataDir.getAbsolutePath();
    }

    private void ensureDirectory() {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
