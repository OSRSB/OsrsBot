package net.runelite.rsb.wrappers.common;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.runelite.cache.definitions.ItemDefinition;
import net.runelite.cache.definitions.NpcDefinition;
import net.runelite.cache.definitions.ObjectDefinition;
import net.runelite.rsb.internal.globval.GlobalConfiguration;
import net.runelite.rsb.wrappers.RSItem;
import net.runelite.rsb.wrappers.RSNPC;
import net.runelite.rsb.wrappers.RSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public interface CacheProvider<T> {
    boolean[] cacheLoads = new boolean[3];
    HashMap<String, File> fileCache = new HashMap<>();
    HashMap<String, Object> definitionCache = new HashMap<>();

    /**
     * Fills the runtime file cache with all the files in the cache directory.
     */
    static void fillFileCache() {
        fillCacheFromDirectory(new File(GlobalConfiguration.Paths.getObjectsCacheDirectory()), RSObject.class.getName());
        fillCacheFromDirectory(new File(GlobalConfiguration.Paths.getItemsCacheDirectory()), RSItem.class.getName());
        fillCacheFromDirectory(new File(GlobalConfiguration.Paths.getNPCsCacheDirectory()), RSNPC.class.getName());
    }

    private static void fillCacheFromDirectory(File dir, String className) {
        File[] directoryListing = dir.listFiles();

        for (File file : directoryListing) {
            if (file.getName().contains(".json")) {
                fileCache.put(Integer.parseInt(file.getName().replace(".json", "")) + className, file);
            }
        }
    }

    /**
     * Adds the definition to the runtime cache.
     *
     * @param id The id of the definition to add
     */
    default void addDefinitionToLoadedCache(int id) {
        try {
            T def = readFileAndGenerateDefinition(fileCache.get(id + this.getClass().getName()));
            if (def != null) {
                if (def instanceof ObjectDefinition) {
                    if (((ObjectDefinition) def).getId() != -1) {
                        definitionCache.put(((ObjectDefinition) def).getId() + this.getClass().getName(), def);
                    }
                }
                if (def instanceof ItemDefinition) {
                    if (((ItemDefinition) def).getId() != -1) {
                        definitionCache.put(((ItemDefinition) def).getId() + this.getClass().getName(), def);
                    }
                }
                if (def instanceof NpcDefinition) {
                    if (((NpcDefinition) def).getId() != -1) {
                        definitionCache.put(((NpcDefinition) def).getId() + this.getClass().getName(), def);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the passed file and generates an object definition from it.
     *
     * @param file The file to read
     * @throws FileNotFoundException If the file is not found
     * @return The object definition generated from the file
     */
    /**
     * Reads the passed file and generates an object definition from it.
     * @param file	The file to read
     * @return	The object definition generated from the file
     * @throws FileNotFoundException    If the file is not found
     */
    default T readFileAndGenerateDefinition(File file) throws FileNotFoundException {
        if (file != null) {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            if (this instanceof RSObject) {
                return gson.fromJson(reader, ObjectDefinition.class);
            }
            if (this instanceof RSItem) {
                return gson.fromJson(reader, ItemDefinition.class);
            }
            if (this instanceof RSNPC) {
                return gson.fromJson(reader, NpcDefinition.class);
            }
        }
        return null;
    }

    /**
     * Creates a new Definition from the passed id.
     *
     * @param id The id of the object definition to create
     * @return The object definition generated from the id
     */
    default Object createDefinition(int id) {
        if (!definitionCache.containsKey(id + this.getClass().getName())) {
            addDefinitionToLoadedCache(id);
        }
        return definitionCache.get(id + this.getClass().getName());
    }
}