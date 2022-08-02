package net.runelite.rsb.wrappers.client_wrapper;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.applet.Applet;
import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/*
Base class for wrapping runelite Client, along with some weird Applet shenanigans.
*/
@SuppressWarnings("removal")
public abstract class BaseClientWrapper extends Applet implements Client {
    public final Client wrappedClient;

    public BaseClientWrapper(Client client) {
        this.wrappedClient = client;
    }

    @Override
    public Component getComponent(int n) {
        return ((Applet) wrappedClient).getComponent(n);
    }

    @Override
    public Callbacks getCallbacks() {
        return wrappedClient.getCallbacks();
    }

    @Override
    public DrawCallbacks getDrawCallbacks() {
        return wrappedClient.getDrawCallbacks();
    }

    @Override
    public void setDrawCallbacks(DrawCallbacks drawCallbacks) {
        wrappedClient.setDrawCallbacks(drawCallbacks);
    }

    @Override
    public String getBuildID() {
        return wrappedClient.getBuildID();
    }

    @Override
    public List<Player> getPlayers() {
        return wrappedClient.getPlayers();
    }

    @Override
    public List<NPC> getNpcs() {
        return wrappedClient.getNpcs();
    }

    @Override
    public NPC[] getCachedNPCs() {
        return wrappedClient.getCachedNPCs();
    }

    @Override
    public Player[] getCachedPlayers() {
        return wrappedClient.getCachedPlayers();
    }

    @Override
    public int getBoostedSkillLevel(Skill skill) {
        return wrappedClient.getBoostedSkillLevel(skill);
    }

    @Override
    public int getRealSkillLevel(Skill skill) {
        return wrappedClient.getRealSkillLevel(skill);
    }

    @Override
    public int getTotalLevel() {
        return wrappedClient.getTotalLevel();
    }

    @Override
    public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender) {
        return wrappedClient.addChatMessage(type, name, message, sender);
    }

    @Override
    public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender, boolean postEvent) {
        return wrappedClient.addChatMessage(type, name, message, sender, postEvent);
    }

    @Override
    public GameState getGameState() {
        return wrappedClient.getGameState();
    }

    @Override
    public void setGameState(GameState gameState) {
        wrappedClient.setGameState(gameState);
    }

    @Override
    public void stopNow() {
        wrappedClient.stopNow();
    }

    @Override
    @Deprecated
    public String getUsername() {
        return wrappedClient.getUsername();
    }

    @Override
    public void setUsername(String name) {
        wrappedClient.setUsername(name);
    }

    @Override
    public void setPassword(String password) {
        wrappedClient.setPassword(password);
    }

    @Override
    public void setOtp(String otp) {
        wrappedClient.setOtp(otp);
    }

    @Override
    public int getCurrentLoginField() {
        return wrappedClient.getCurrentLoginField();
    }

    @Override
    public int getLoginIndex() {
        return wrappedClient.getLoginIndex();
    }

    @Override
    public AccountType getAccountType() {
        return wrappedClient.getAccountType();
    }

    @Override
    public Canvas getCanvas() {
        return wrappedClient.getCanvas();
    }

    @Override
    public int getFPS() {
        return wrappedClient.getFPS();
    }

    @Override
    public int getCameraX() {
        return wrappedClient.getCameraX();
    }

    @Override
    public int getCameraY() {
        return wrappedClient.getCameraY();
    }

    @Override
    public int getCameraZ() {
        return wrappedClient.getCameraZ();
    }

    @Override
    public int getCameraPitch() {
        return wrappedClient.getCameraPitch();
    }

    @Override
    public int getCameraYaw() {
        return wrappedClient.getCameraYaw();
    }

    @Override
    public int getWorld() {
        return wrappedClient.getWorld();
    }

    @Override
    public int getCanvasHeight() {
        return wrappedClient.getCanvasHeight();
    }

    @Override
    public int getCanvasWidth() {
        return wrappedClient.getCanvasWidth();
    }

    @Override
    public int getViewportHeight() {
        return wrappedClient.getViewportHeight();
    }

    @Override
    public int getViewportWidth() {
        return wrappedClient.getViewportWidth();
    }

    @Override
    public int getViewportXOffset() {
        return wrappedClient.getViewportXOffset();
    }

    @Override
    public int getViewportYOffset() {
        return wrappedClient.getViewportYOffset();
    }

    @Override
    public int getScale() {
        return wrappedClient.getScale();
    }

    @Override
    public Point getMouseCanvasPosition() {
        return wrappedClient.getMouseCanvasPosition();
    }

    @Override
    public int[][][] getTileHeights() {
        return wrappedClient.getTileHeights();
    }

    @Override
    public byte[][][] getTileSettings() {
        return wrappedClient.getTileSettings();
    }

    @Override
    public int getPlane() {
        return wrappedClient.getPlane();
    }

    @Override
    public Scene getScene() {
        return wrappedClient.getScene();
    }

    @Override
    public Player getLocalPlayer() {
        return wrappedClient.getLocalPlayer();
    }

    @Override
    @Nullable
    public NPC getFollower() {
        return wrappedClient.getFollower();
    }

    @Override
    @Nonnull
    public ItemComposition getItemDefinition(int id) {
        return wrappedClient.getItemDefinition(id);
    }

    @Override
    @Nullable
    public SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted, int scale) {
        return wrappedClient.createItemSprite(itemId, quantity, border, shadowColor, stackable, noted, scale);
    }

    @Override
    public NodeCache getItemModelCache() {
        return wrappedClient.getItemModelCache();
    }

    @Override
    public NodeCache getItemSpriteCache() {
        return wrappedClient.getItemSpriteCache();
    }

    @Override
    @Nullable
    public SpritePixels[] getSprites(IndexDataBase source, int archiveId, int fileId) {
        return wrappedClient.getSprites(source, archiveId, fileId);
    }

    @Override
    public IndexDataBase getIndexSprites() {
        return wrappedClient.getIndexSprites();
    }

    @Override
    public IndexDataBase getIndexScripts() {
        return wrappedClient.getIndexScripts();
    }

    @Override
    public IndexDataBase getIndexConfig() {
        return wrappedClient.getIndexConfig();
    }

    @Override
    public IndexDataBase getIndex(int id) {
        return wrappedClient.getIndex(id);
    }

    @Override
    public int getBaseX() {
        return wrappedClient.getBaseX();
    }

    @Override
    public int getBaseY() {
        return wrappedClient.getBaseY();
    }

    @Override
    public int getMouseCurrentButton() {
        return wrappedClient.getMouseCurrentButton();
    }

    @Override
    @Nullable
    public Tile getSelectedSceneTile() {
        return wrappedClient.getSelectedSceneTile();
    }

    @Override
    public boolean isDraggingWidget() {
        return wrappedClient.isDraggingWidget();
    }

    @Override
    @Nullable
    public Widget getDraggedWidget() {
        return wrappedClient.getDraggedWidget();
    }

    @Override
    @Nullable
    public Widget getDraggedOnWidget() {
        return wrappedClient.getDraggedOnWidget();
    }

    @Override
    public void setDraggedOnWidget(Widget widget) {
        wrappedClient.setDraggedOnWidget(widget);
    }

    @Override
    public int getDragTime() {
        return wrappedClient.getDragTime();
    }

    @Override
    public int getTopLevelInterfaceId() {
        return wrappedClient.getTopLevelInterfaceId();
    }

    @Override
    public Widget[] getWidgetRoots() {
        return wrappedClient.getWidgetRoots();
    }

    @Override
    @Nullable
    public Widget getWidget(WidgetInfo widget) {
        return wrappedClient.getWidget(widget);
    }

    @Override
    @Nullable
    public Widget getWidget(int groupId, int childId) {
        return wrappedClient.getWidget(groupId, childId);
    }

    @Override
    @Nullable
    public Widget getWidget(int packedID) {
        return wrappedClient.getWidget(packedID);
    }

    @Override
    public int[] getWidgetPositionsX() {
        return wrappedClient.getWidgetPositionsX();
    }

    @Override
    public int[] getWidgetPositionsY() {
        return wrappedClient.getWidgetPositionsY();
    }

    @Override
    public int getEnergy() {
        return wrappedClient.getEnergy();
    }

    @Override
    public int getWeight() {
        return wrappedClient.getWeight();
    }

    @Override
    public String[] getPlayerOptions() {
        return wrappedClient.getPlayerOptions();
    }

    @Override
    public boolean[] getPlayerOptionsPriorities() {
        return wrappedClient.getPlayerOptionsPriorities();
    }

    @Override
    public int[] getPlayerMenuTypes() {
        return wrappedClient.getPlayerMenuTypes();
    }

    @Override
    public World[] getWorldList() {
        return wrappedClient.getWorldList();
    }

    @Override
    public MenuEntry createMenuEntry(int idx) {
        return wrappedClient.createMenuEntry(idx);
    }

    @Override
    public MenuEntry[] getMenuEntries() {
        return wrappedClient.getMenuEntries();
    }

    @Override
    public void setMenuEntries(MenuEntry[] entries) {
        wrappedClient.setMenuEntries(entries);
    }

    @Override
    public boolean isMenuOpen() {
        return wrappedClient.isMenuOpen();
    }

    @Override
    public int getMenuX() {
        return wrappedClient.getMenuX();
    }

    @Override
    public int getMenuY() {
        return wrappedClient.getMenuY();
    }

    @Override
    public int getMenuHeight() {
        return wrappedClient.getMenuHeight();
    }

    @Override
    public int getMenuWidth() {
        return wrappedClient.getMenuWidth();
    }

    @Override
    public int getMapAngle() {
        return wrappedClient.getMapAngle();
    }

    @Override
    public void setCameraYawTarget(int cameraYawTarget) {
        wrappedClient.setCameraYawTarget(cameraYawTarget);
    }

    @Override
    public boolean isResized() {
        return wrappedClient.isResized();
    }

    @Override
    public int getRevision() {
        return wrappedClient.getRevision();
    }

    @Override
    public int[] getMapRegions() {
        return wrappedClient.getMapRegions();
    }

    @Override
    public int[][][] getInstanceTemplateChunks() {
        return wrappedClient.getInstanceTemplateChunks();
    }

    @Override
    public int[][] getXteaKeys() {
        return wrappedClient.getXteaKeys();
    }

    @Override
    public int[] getVarps() {
        return wrappedClient.getVarps();
    }

    @Override
    public int[] getServerVarps() {
        return wrappedClient.getServerVarps();
    }

    @Override
    public Map<Integer, Object> getVarcMap() {
        return wrappedClient.getVarcMap();
    }

    @Override
    public int getVar(VarPlayer varPlayer) {
        return wrappedClient.getVar(varPlayer);
    }

    @Override
    public int getServerVar(VarPlayer varPlayer) {
        return wrappedClient.getServerVar(varPlayer);
    }

    @Override
    @Deprecated
    public int getVar(int varbit) {
        return wrappedClient.getVar(varbit);
    }

    @Override
    public int getVarbitValue(int varbit) {
        return wrappedClient.getVarbitValue(varbit);
    }

    @Override
    public int getServerVarbitValue(int varbit) {
        return wrappedClient.getServerVarbitValue(varbit);
    }

    @Override
    public int getVarpValue(int varpId) {
        return wrappedClient.getVarpValue(varpId);
    }

    @Override
    public int getServerVarpValue(int varpId) {
        return wrappedClient.getServerVarpValue(varpId);
    }

    @Override
    public int getVarcIntValue(int var) {
        return wrappedClient.getVarcIntValue(var);
    }

    @Override
    public String getVarcStrValue(int var) {
        return wrappedClient.getVarcStrValue(var);
    }

    @Override
    public void setVarcStrValue(int var, String value) {
        wrappedClient.setVarcStrValue(var, value);
    }

    @Override
    public void setVarcIntValue(int var, int value) {
        wrappedClient.setVarcIntValue(var, value);
    }

    @Override
    public void setVarbit(int varbit, int value) {
        wrappedClient.setVarbit(varbit, value);
    }

    @Override
    @Nullable
    public VarbitComposition getVarbit(int id) {
        return wrappedClient.getVarbit(id);
    }

    @Override
    public int getVarbitValue(int[] varps, int varbitId) {
        return wrappedClient.getVarbitValue(varps, varbitId);
    }

    @Override
    public void setVarbitValue(int[] varps, int varbit, int value) {
        wrappedClient.setVarbitValue(varps, varbit, value);
    }

    @Override
    public void queueChangedVarp(int varp) {
        wrappedClient.queueChangedVarp(varp);
    }

    @Override
    public HashTable<IntegerNode> getWidgetFlags() {
        return wrappedClient.getWidgetFlags();
    }

    @Override
    public HashTable<WidgetNode> getComponentTable() {
        return wrappedClient.getComponentTable();
    }

    @Override
    public GrandExchangeOffer[] getGrandExchangeOffers() {
        return wrappedClient.getGrandExchangeOffers();
    }

    @Override
    public boolean isPrayerActive(Prayer prayer) {
        return wrappedClient.isPrayerActive(prayer);
    }

    @Override
    public int getSkillExperience(Skill skill) {
        return wrappedClient.getSkillExperience(skill);
    }

    @Override
    public long getOverallExperience() {
        return wrappedClient.getOverallExperience();
    }

    @Override
    public void refreshChat() {
        wrappedClient.refreshChat();
    }

    @Override
    public Map<Integer, ChatLineBuffer> getChatLineMap() {
        return wrappedClient.getChatLineMap();
    }

    @Override
    public IterableHashTable<MessageNode> getMessages() {
        return wrappedClient.getMessages();
    }

    @Override
    public ObjectComposition getObjectDefinition(int objectId) {
        return wrappedClient.getObjectDefinition(objectId);
    }

    @Override
    public NPCComposition getNpcDefinition(int npcId) {
        return wrappedClient.getNpcDefinition(npcId);
    }

    @Override
    public StructComposition getStructComposition(int structID) {
        return wrappedClient.getStructComposition(structID);
    }

    @Override
    public NodeCache getStructCompositionCache() {
        return wrappedClient.getStructCompositionCache();
    }

    @Override
    public Object getDBTableField(int rowID, int column, int tupleIndex, int fieldIndex) {
        return wrappedClient.getDBTableField(rowID, column, tupleIndex, fieldIndex);
    }

    @Override
    public MapElementConfig[] getMapElementConfigs() {
        return wrappedClient.getMapElementConfigs();
    }

    @Override
    public IndexedSprite[] getMapScene() {
        return wrappedClient.getMapScene();
    }

    @Override
    public SpritePixels[] getMapDots() {
        return wrappedClient.getMapDots();
    }

    @Override
    public int getGameCycle() {
        return wrappedClient.getGameCycle();
    }

    @Override
    public SpritePixels[] getMapIcons() {
        return wrappedClient.getMapIcons();
    }

    @Override
    public IndexedSprite[] getModIcons() {
        return wrappedClient.getModIcons();
    }

    @Override
    public void setModIcons(IndexedSprite[] modIcons) {
        wrappedClient.setModIcons(modIcons);
    }

    @Override
    public IndexedSprite createIndexedSprite() {
        return wrappedClient.createIndexedSprite();
    }

    @Override
    public SpritePixels createSpritePixels(int[] pixels, int width, int height) {
        return wrappedClient.createSpritePixels(pixels, width, height);
    }

    @Override
    @Nullable
    public LocalPoint getLocalDestinationLocation() {
        return wrappedClient.getLocalDestinationLocation();
    }

    @Override
    public Projectile createProjectile(int id, int plane, int startX, int startY, int startZ, int startCycle, int endCycle, int slope, int startHeight, int endHeight, @Nullable Actor target, int targetX, int targetY) {
        return wrappedClient.createProjectile(id, plane, startX, startY, startZ, startCycle, endCycle, slope, startHeight, endHeight, target, targetX, targetY);
    }

    @Override
    public Deque<Projectile> getProjectiles() {
        return wrappedClient.getProjectiles();
    }

    @Override
    public Deque<GraphicsObject> getGraphicsObjects() {
        return wrappedClient.getGraphicsObjects();
    }

    @Override
    public RuneLiteObject createRuneLiteObject() {
        return wrappedClient.createRuneLiteObject();
    }

    @Override
    @Nullable
    public ModelData loadModelData(int id) {
        return wrappedClient.loadModelData(id);
    }

    @Override
    public ModelData mergeModels(ModelData[] models, int length) {
        return wrappedClient.mergeModels(models, length);
    }

    @Override
    public ModelData mergeModels(ModelData... models) {
        return wrappedClient.mergeModels(models);
    }

    @Override
    @Nullable
    public Model loadModel(int id) {
        return wrappedClient.loadModel(id);
    }

    @Override
    @Nullable
    public Model loadModel(int id, short[] colorToFind, short[] colorToReplace) {
        return wrappedClient.loadModel(id, colorToFind, colorToReplace);
    }

    @Override
    public Animation loadAnimation(int id) {
        return wrappedClient.loadAnimation(id);
    }

    @Override
    public int getMusicVolume() {
        return wrappedClient.getMusicVolume();
    }

    @Override
    public void setMusicVolume(int volume) {
        wrappedClient.setMusicVolume(volume);
    }

    @Override
    public boolean isPlayingJingle() {
        return wrappedClient.isPlayingJingle();
    }

    @Override
    public int getMusicCurrentTrackId() {
        return wrappedClient.getMusicCurrentTrackId();
    }

    @Override
    public void playSoundEffect(int id) {
        wrappedClient.playSoundEffect(id);
    }

    @Override
    public void playSoundEffect(int id, int x, int y, int range) {
        wrappedClient.playSoundEffect(id, x, y, range);
    }

    @Override
    public void playSoundEffect(int id, int x, int y, int range, int delay) {
        wrappedClient.playSoundEffect(id, x, y, range, delay);
    }

    @Override
    public void playSoundEffect(int id, int volume) {
        wrappedClient.playSoundEffect(id, volume);
    }

    @Override
    public BufferProvider getBufferProvider() {
        return wrappedClient.getBufferProvider();
    }

    @Override
    public int getMouseIdleTicks() {
        return wrappedClient.getMouseIdleTicks();
    }

    @Override
    public long getMouseLastPressedMillis() {
        return wrappedClient.getMouseLastPressedMillis();
    }

    @Override
    public int getKeyboardIdleTicks() {
        return wrappedClient.getKeyboardIdleTicks();
    }

    @Override
    public void changeMemoryMode(boolean lowMemory) {
        wrappedClient.changeMemoryMode(lowMemory);
    }

    @Override
    @Nullable
    public ItemContainer getItemContainer(InventoryID inventory) {
        return wrappedClient.getItemContainer(inventory);
    }

    @Override
    @Nullable
    public ItemContainer getItemContainer(int id) {
        return wrappedClient.getItemContainer(id);
    }

    @Override
    public HashTable<ItemContainer> getItemContainers() {
        return wrappedClient.getItemContainers();
    }

    @Override
    public int getIntStackSize() {
        return wrappedClient.getIntStackSize();
    }

    @Override
    public void setIntStackSize(int stackSize) {
        wrappedClient.setIntStackSize(stackSize);
    }

    @Override
    public int[] getIntStack() {
        return wrappedClient.getIntStack();
    }

    @Override
    public int getStringStackSize() {
        return wrappedClient.getStringStackSize();
    }

    @Override
    public void setStringStackSize(int stackSize) {
        wrappedClient.setStringStackSize(stackSize);
    }

    @Override
    public String[] getStringStack() {
        return wrappedClient.getStringStack();
    }

    @Override
    public Widget getScriptActiveWidget() {
        return wrappedClient.getScriptActiveWidget();
    }

    @Override
    public Widget getScriptDotWidget() {
        return wrappedClient.getScriptDotWidget();
    }

    @Override
    public boolean isFriended(String name, boolean mustBeLoggedIn) {
        return wrappedClient.isFriended(name, mustBeLoggedIn);
    }

    @Override
    @Nullable
    public FriendsChatManager getFriendsChatManager() {
        return wrappedClient.getFriendsChatManager();
    }

    @Override
    public FriendContainer getFriendContainer() {
        return wrappedClient.getFriendContainer();
    }

    @Override
    public NameableContainer<Ignore> getIgnoreContainer() {
        return wrappedClient.getIgnoreContainer();
    }

    @Override
    public Preferences getPreferences() {
        return wrappedClient.getPreferences();
    }

    @Override
    public void setCameraPitchRelaxerEnabled(boolean enabled) {
        wrappedClient.setCameraPitchRelaxerEnabled(enabled);
    }

    @Override
    public void setInvertYaw(boolean invertYaw) {
        wrappedClient.setInvertYaw(invertYaw);
    }

    @Override
    public void setInvertPitch(boolean invertPitch) {
        wrappedClient.setInvertPitch(invertPitch);
    }

    @Override
    public RenderOverview getRenderOverview() {
        return wrappedClient.getRenderOverview();
    }

    @Override
    public boolean isStretchedEnabled() {
        return wrappedClient.isStretchedEnabled();
    }

    @Override
    public void setStretchedEnabled(boolean state) {
        wrappedClient.setStretchedEnabled(state);
    }

    @Override
    public boolean isStretchedFast() {
        return wrappedClient.isStretchedFast();
    }

    @Override
    public void setStretchedFast(boolean state) {
        wrappedClient.setStretchedFast(state);
    }

    @Override
    public void setStretchedIntegerScaling(boolean state) {
        wrappedClient.setStretchedIntegerScaling(state);
    }

    @Override
    public void setStretchedKeepAspectRatio(boolean state) {
        wrappedClient.setStretchedKeepAspectRatio(state);
    }

    @Override
    public void setScalingFactor(int factor) {
        wrappedClient.setScalingFactor(factor);
    }

    @Override
    public void invalidateStretching(boolean resize) {
        wrappedClient.invalidateStretching(resize);
    }

    @Override
    public Dimension getStretchedDimensions() {
        return wrappedClient.getStretchedDimensions();
    }

    @Override
    public Dimension getRealDimensions() {
        return wrappedClient.getRealDimensions();
    }

    @Override
    public void changeWorld(World world) {
        wrappedClient.changeWorld(world);
    }

    @Override
    public World createWorld() {
        return wrappedClient.createWorld();
    }

    @Override
    public SpritePixels drawInstanceMap(int z) {
        return wrappedClient.drawInstanceMap(z);
    }

    @Override
    public void runScript(Object... args) {
        wrappedClient.runScript(args);
    }

    @Override
    public ScriptEvent createScriptEvent(Object... args) {
        return wrappedClient.createScriptEvent(args);
    }

    @Override
    public boolean hasHintArrow() {
        return wrappedClient.hasHintArrow();
    }

    @Override
    public HintArrowType getHintArrowType() {
        return wrappedClient.getHintArrowType();
    }

    @Override
    public void clearHintArrow() {
        wrappedClient.clearHintArrow();
    }

    @Override
    public void setHintArrow(WorldPoint point) {
        wrappedClient.setHintArrow(point);
    }

    @Override
    public void setHintArrow(Player player) {
        wrappedClient.setHintArrow(player);
    }

    @Override
    public void setHintArrow(NPC npc) {
        wrappedClient.setHintArrow(npc);
    }

    @Override
    public WorldPoint getHintArrowPoint() {
        return wrappedClient.getHintArrowPoint();
    }

    @Override
    public Player getHintArrowPlayer() {
        return wrappedClient.getHintArrowPlayer();
    }

    @Override
    public NPC getHintArrowNpc() {
        return wrappedClient.getHintArrowNpc();
    }

    @Override
    public boolean isInterpolatePlayerAnimations() {
        return wrappedClient.isInterpolatePlayerAnimations();
    }

    @Override
    public void setInterpolatePlayerAnimations(boolean interpolate) {
        wrappedClient.setInterpolatePlayerAnimations(interpolate);
    }

    @Override
    public boolean isInterpolateNpcAnimations() {
        return wrappedClient.isInterpolateNpcAnimations();
    }

    @Override
    public void setInterpolateNpcAnimations(boolean interpolate) {
        wrappedClient.setInterpolateNpcAnimations(interpolate);
    }

    @Override
    public boolean isInterpolateObjectAnimations() {
        return wrappedClient.isInterpolateObjectAnimations();
    }

    @Override
    public void setInterpolateObjectAnimations(boolean interpolate) {
        wrappedClient.setInterpolateObjectAnimations(interpolate);
    }

    @Override
    public boolean isInInstancedRegion() {
        return wrappedClient.isInInstancedRegion();
    }

    @Override
    public int getItemPressedDuration() {
        return wrappedClient.getItemPressedDuration();
    }

    @Override
    @Nullable
    public CollisionData[] getCollisionMaps() {
        return wrappedClient.getCollisionMaps();
    }

    @Override
    public int[] getBoostedSkillLevels() {
        return wrappedClient.getBoostedSkillLevels();
    }

    @Override
    public int[] getRealSkillLevels() {
        return wrappedClient.getRealSkillLevels();
    }

    @Override
    public int[] getSkillExperiences() {
        return wrappedClient.getSkillExperiences();
    }

    @Override
    public void queueChangedSkill(Skill skill) {
        wrappedClient.queueChangedSkill(skill);
    }

    @Override
    public Map<Integer, SpritePixels> getSpriteOverrides() {
        return wrappedClient.getSpriteOverrides();
    }

    @Override
    public Map<Integer, SpritePixels> getWidgetSpriteOverrides() {
        return wrappedClient.getWidgetSpriteOverrides();
    }

    @Override
    public void setCompass(SpritePixels spritePixels) {
        wrappedClient.setCompass(spritePixels);
    }

    @Override
    public NodeCache getWidgetSpriteCache() {
        return wrappedClient.getWidgetSpriteCache();
    }

    @Override
    public int getTickCount() {
        return wrappedClient.getTickCount();
    }

    @Override
    public void setTickCount(int tickCount) {
        wrappedClient.setTickCount(tickCount);
    }

    @Override
    @Deprecated
    public void setInventoryDragDelay(int delay) {
        wrappedClient.setInventoryDragDelay(delay);
    }

    @Override
    public EnumSet<WorldType> getWorldType() {
        return wrappedClient.getWorldType();
    }

    @Override
    public int getOculusOrbState() {
        return wrappedClient.getOculusOrbState();
    }

    @Override
    public void setOculusOrbState(int state) {
        wrappedClient.setOculusOrbState(state);
    }

    @Override
    public void setOculusOrbNormalSpeed(int speed) {
        wrappedClient.setOculusOrbNormalSpeed(speed);
    }

    @Override
    public int getOculusOrbFocalPointX() {
        return wrappedClient.getOculusOrbFocalPointX();
    }

    @Override
    public int getOculusOrbFocalPointY() {
        return wrappedClient.getOculusOrbFocalPointY();
    }

    @Override
    public void openWorldHopper() {
        wrappedClient.openWorldHopper();
    }

    @Override
    public void hopToWorld(World world) {
        wrappedClient.hopToWorld(world);
    }

    @Override
    public void setSkyboxColor(int skyboxColor) {
        wrappedClient.setSkyboxColor(skyboxColor);
    }

    @Override
    public int getSkyboxColor() {
        return wrappedClient.getSkyboxColor();
    }

    @Override
    public boolean isGpu() {
        return wrappedClient.isGpu();
    }

    @Override
    public void setGpu(boolean gpu) {
        wrappedClient.setGpu(gpu);
    }

    @Override
    public int get3dZoom() {
        return wrappedClient.get3dZoom();
    }

    @Override
    public int getCenterX() {
        return wrappedClient.getCenterX();
    }

    @Override
    public int getCenterY() {
        return wrappedClient.getCenterY();
    }

    @Override
    public int getCameraX2() {
        return wrappedClient.getCameraX2();
    }

    @Override
    public int getCameraY2() {
        return wrappedClient.getCameraY2();
    }

    @Override
    public int getCameraZ2() {
        return wrappedClient.getCameraZ2();
    }

    @Override
    public TextureProvider getTextureProvider() {
        return wrappedClient.getTextureProvider();
    }

    @Override
    public void setRenderArea(boolean[][] renderArea) {
        wrappedClient.setRenderArea(renderArea);
    }

    @Override
    public int getRasterizer3D_clipMidX2() {
        return wrappedClient.getRasterizer3D_clipMidX2();
    }

    @Override
    public int getRasterizer3D_clipNegativeMidX() {
        return wrappedClient.getRasterizer3D_clipNegativeMidX();
    }

    @Override
    public int getRasterizer3D_clipNegativeMidY() {
        return wrappedClient.getRasterizer3D_clipNegativeMidY();
    }

    @Override
    public int getRasterizer3D_clipMidY2() {
        return wrappedClient.getRasterizer3D_clipMidY2();
    }

    @Override
    public void checkClickbox(Model model, int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
        wrappedClient.checkClickbox(model, orientation, pitchSin, pitchCos, yawSin, yawCos, x, y, z, hash);
    }

    @Override
    @Deprecated
    public Widget getIf1DraggedWidget() {
        return wrappedClient.getIf1DraggedWidget();
    }

    @Override
    @Deprecated
    public int getIf1DraggedItemIndex() {
        return wrappedClient.getIf1DraggedItemIndex();
    }

    @Override
    public boolean getSpellSelected() {
        return wrappedClient.getSpellSelected();
    }

    @Override
    public void setSpellSelected(boolean selected) {
        wrappedClient.setSpellSelected(selected);
    }

    @Override
    public int getSelectedItem() {
        return wrappedClient.getSelectedItem();
    }

    @Override
    public int getSelectedItemIndex() {
        return wrappedClient.getSelectedItemIndex();
    }

    @Override
    @Nullable
    public Widget getSelectedWidget() {
        return wrappedClient.getSelectedWidget();
    }

    @Override
    public NodeCache getItemCompositionCache() {
        return wrappedClient.getItemCompositionCache();
    }

    @Override
    public NodeCache getObjectCompositionCache() {
        return wrappedClient.getObjectCompositionCache();
    }

    @Override
    public SpritePixels[] getCrossSprites() {
        return wrappedClient.getCrossSprites();
    }

    @Override
    public EnumComposition getEnum(int id) {
        return wrappedClient.getEnum(id);
    }

    @Override
    public void draw2010Menu(int alpha) {
        wrappedClient.draw2010Menu(alpha);
    }

    @Override
    public void drawOriginalMenu(int alpha) {
        wrappedClient.drawOriginalMenu(alpha);
    }

    @Override
    public void resetHealthBarCaches() {
        wrappedClient.resetHealthBarCaches();
    }

    @Override
    public int getItemCount() {
        return wrappedClient.getItemCount();
    }

    @Override
    public void setAllWidgetsAreOpTargetable(boolean value) {
        wrappedClient.setAllWidgetsAreOpTargetable(value);
    }

    @Override
    public void setGeSearchResultCount(int count) {
        wrappedClient.setGeSearchResultCount(count);
    }

    @Override
    public void setGeSearchResultIds(short[] ids) {
        wrappedClient.setGeSearchResultIds(ids);
    }

    @Override
    public void setGeSearchResultIndex(int index) {
        wrappedClient.setGeSearchResultIndex(index);
    }

    @Override
    public void setLoginScreen(SpritePixels pixels) {
        wrappedClient.setLoginScreen(pixels);
    }

    @Override
    public void setShouldRenderLoginScreenFire(boolean val) {
        wrappedClient.setShouldRenderLoginScreenFire(val);
    }

    @Override
    public boolean isKeyPressed(int keycode) {
        return wrappedClient.isKeyPressed(keycode);
    }

    @Override
    public long[] getCrossWorldMessageIds() {
        return wrappedClient.getCrossWorldMessageIds();
    }

    @Override
    public int getCrossWorldMessageIdsIndex() {
        return wrappedClient.getCrossWorldMessageIdsIndex();
    }

    @Override
    @Nullable
    public ClanChannel getClanChannel() {
        return wrappedClient.getClanChannel();
    }

    @Override
    @Nullable
    public ClanChannel getGuestClanChannel() {
        return wrappedClient.getGuestClanChannel();
    }

    @Override
    @Nullable
    public ClanSettings getClanSettings() {
        return wrappedClient.getClanSettings();
    }

    @Override
    @Nullable
    public ClanSettings getGuestClanSettings() {
        return wrappedClient.getGuestClanSettings();
    }

    @Override
    @Nullable
    public ClanChannel getClanChannel(int clanId) {
        return wrappedClient.getClanChannel(clanId);
    }

    @Override
    @Nullable
    public ClanSettings getClanSettings(int clanId) {
        return wrappedClient.getClanSettings(clanId);
    }

    @Override
    public void setUnlockedFps(boolean unlock) {
        wrappedClient.setUnlockedFps(unlock);
    }

    @Override
    public void setUnlockedFpsTarget(int fps) {
        wrappedClient.setUnlockedFpsTarget(fps);
    }

    @Override
    public Deque<AmbientSoundEffect> getAmbientSoundEffects() {
        return wrappedClient.getAmbientSoundEffects();
    }

    @Override
    public long getAccountHash() {
        return wrappedClient.getAccountHash();
    }

    @Override
    public Thread getClientThread() {
        return wrappedClient.getClientThread();
    }

    @Override
    public boolean isClientThread() {
        return wrappedClient.isClientThread();
    }

    @Override
    public void resizeCanvas() {
        wrappedClient.resizeCanvas();
    }
}
