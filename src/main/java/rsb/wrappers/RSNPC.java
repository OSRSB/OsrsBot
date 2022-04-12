package rsb.wrappers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.runelite.api.*;
import net.runelite.cache.definitions.NpcDefinition;
import rsb.internal.globval.GlobalConfiguration;
import rsb.methods.MethodContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class RSNPC extends RSCharacter {
    private static HashMap<Integer, NpcDefinition> npcDefinitionCache;
    private static HashMap<Integer, File> npcFileCache;
    private final SoftReference<NPC> npc;
    private final NpcDefinition def;
    //private final NPC npc;

    public RSNPC(final MethodContext ctx, final NPC npc) {
        super(ctx);
        this.npc =  new SoftReference<>(npc);
        this.def = (npc.getId() != -1) ? createObjectDefinition(npc.getId()) : null;
    }

    /**
     * Fills the runtime file cache with all the files in the cache directory.
     */
    private void fillFileCache() {
        try {
            File dir = new File(GlobalConfiguration.Paths.getNPCsCacheDirectory());
            File[] directoryListing = dir.listFiles();
            npcFileCache = new HashMap<>();
            npcDefinitionCache = new HashMap<>();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    if (file.getName().contains(".json")) {
                        npcFileCache.put(Integer.parseInt(file.getName().replace(".json", "")), file);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds the object definition to the runtime cache.
     * @param id	The id of the object definition to add
     */
    private void addDefinitionToLoadedCache(int id) {
        try {
            NpcDefinition def = readFileAndGenerateDefinition(npcFileCache.get(id));
            if (def != null && def.getId() != -1) {
                npcDefinitionCache.put(def.getId(), def);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the passed file and generates an object definition from it.
     * @param file	The file to read
     * @return	The object definition generated from the file
     * @throws FileNotFoundException    If the file is not found
     */
    public static NpcDefinition readFileAndGenerateDefinition(File file) throws FileNotFoundException {
        if (file != null) {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            return gson.fromJson(reader, NpcDefinition.class);
        }
        return null;
    }

    /**
     * Creates a new ObjectDefinition from the passed id.
     * @param id	The id of the object definition to create
     * @return	The object definition generated from the id
     */
    private NpcDefinition createObjectDefinition(int id) {
        if (npcFileCache == null || npcFileCache.isEmpty()) {
            fillFileCache();
        }
        if (!npcDefinitionCache.containsKey(id)) {
            addDefinitionToLoadedCache(id);
        }
        return (npcDefinitionCache != null) ? npcDefinitionCache.get(id) : null;
    }

    @Override
    public Actor getAccessor() {
        return npc.get();
    }

    @Override
    public Actor getInteracting() {
        return getAccessor().getInteracting();
    }

    public String[] getActions() {
        NpcDefinition def = getDef();

        if (def != null) {
            return def.getActions();
        }
        return new String[0];
    }

    public int getID() {
        NpcDefinition def = getDef();
        if (def != null) {
            return def.getId();
        }
        return -1;
    }

    @Override
    public String getName() {
        NpcDefinition def = getDef();
        if (def != null) {
            return def.getName();
        }
        return "";
    }

    @Override
    public int getLevel() {
        NPC c = npc.get();
        if (c == null) {
            return -1;
        } else {
            return c.getCombatLevel();
        }
    }

    /**
     * @return <code>true</code> if RSNPC is interacting with RSPlayer; otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean isInteractingWithLocalPlayer() {
        RSNPC npc = this;
        return npc.getInteracting() != null
                && npc.getInteracting().equals(methods.players.getMyPlayer());
    }

    /*
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final String act : getActions()) {
            sb.append(act);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return "NPC[" + getName() + "],actions=[" + sb.toString() + "]"
                + super.toString();
    }*/

    NpcDefinition getDef() {
        return this.def;
    }

    public RSTile getPosition() {
        return getLocation();
    }
}
