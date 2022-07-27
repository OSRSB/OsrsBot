package net.runelite.client.modified;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.client.RuneLiteProperties;

public class BotProperties extends RuneLiteProperties {
    @Getter(AccessLevel.PUBLIC)
    private static final Properties properties = new Properties();

    static {
        try (InputStream in = RuneLiteProperties.class.getResourceAsStream("runelite.properties")) {
            properties.load(in);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}