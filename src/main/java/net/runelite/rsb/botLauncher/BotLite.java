package net.runelite.rsb.botLauncher;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MainBufferProvider;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NPCManager;
import net.runelite.client.modified.RuneLite;
import net.runelite.rsb.event.EventManager;
import net.runelite.rsb.event.events.PaintEvent;
import net.runelite.rsb.event.events.TextPaintEvent;
import net.runelite.rsb.internal.BreakHandler;
import net.runelite.rsb.internal.InputManager;
import net.runelite.rsb.internal.PassiveScriptHandler;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.input.Canvas;
import net.runelite.rsb.methods.Environment;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.client.plugins.bot.BotPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.plugins.bot.BotPanel;
import net.runelite.client.plugins.bot.AccountPanel;
import net.runelite.client.plugins.bot.ScriptPanel;
import net.runelite.client.plugins.bot.base.DebugPanel;
import net.runelite.rsb.plugin.AccountManager;
import net.runelite.rsb.plugin.ScriptSelector;
import net.runelite.rsb.service.ScriptDefinition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.EventListener;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;

@Singleton
@Slf4j
@SuppressWarnings("removal")
public class BotLite extends RuneLite implements BotLiteInterface {
    private String account;
    private MethodContext methods;
    private Component panel;
    private PaintEvent paintEvent;
    private TextPaintEvent textPaintEvent;
    private EventManager eventManager;
    private BufferedImage backBuffer;
    private Image image;
    private InputManager im;
    private ScriptHandler sh;
    private PassiveScriptHandler psh;
    private BreakHandler bh;
    private Map<String, EventListener> listeners;
    private boolean kill_passive = false;
    private Canvas canvas;

    /**
     * Defines what types of input are enabled when overrideInput is false.
     * Defaults to 'keyboard only' whenever a script is started.
     */
    public volatile int inputFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;

    /**
     * whether user input is allowed despite a script's preference.
     */
    public volatile boolean overrideInput = false;

    /**
     * whether all anti-randoms are enabled.
     */
    public volatile boolean disableRandoms = false;

    /**
     * whether the login screen anti-random is enabled.
     */
    public volatile boolean disableAutoLogin = false;

    /**
     * whether rendering is enabled.
     */
    public volatile boolean disableRendering = false;

    /**
     * whether the canvas is enabled.
     */
    public volatile boolean disableCanvas = false;

    /**
     * Set the canvas to the opposite state
     */
    public void changeCanvasState() {
        if (disableCanvas) {
            getLoader().setVisible(false);
            return;
        }
        getLoader().setVisible(true);
    }

    public String getAccountName() {
        return account;
    }

    public Client getClient() {
        return client = injector.getInstance(Client.class);
    }

    // Applet API removed in RuneLite 1.12+

    public ItemManager getItemManager() { return injector.getInstance(ItemManager.class);}

    public NPCManager getNPCManager() { return injector.getInstance(NPCManager.class);}

    public MethodContext getMethodContext() {
        return methods;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public InputManager getInputManager() {
        return im;
    }

    public BreakHandler getBreakHandler() {
        return bh;
    }

    public ScriptHandler getScriptHandler() {
        return sh;
    }

    public PassiveScriptHandler getPassiveScriptHandler() {
        return psh;
    }

    public void addListener(Class<?> clazz) {
        EventListener el = instantiateListener(clazz);
        listeners.put(clazz.getName(), el);
        eventManager.addListener(el);
    }

    public void removeListener(Class<?> clazz) {
        EventListener el = listeners.get(clazz.getName());
        listeners.remove(clazz.getName());
        eventManager.removeListener(el);
    }

    private EventListener instantiateListener(Class<?> clazz) {
        try {
            EventListener listener;
            try {
                Constructor<?> constructor = clazz.getConstructor(RuneLite.class);
                listener = (EventListener) constructor.newInstance(this);
            } catch (Exception e) {
                listener = clazz.asSubclass(EventListener.class).newInstance();
            }
            return listener;
        } catch (Exception ignored) {
            log.debug("Failed to instantiate listener", ignored);
        }
        return null;
    }

    public boolean hasListener(Class<?> clazz) {
        return clazz != null && listeners.get(clazz.getName()) != null;
    }

    public Image getImage() {
        return image;
    }

    public BufferedImage getBackBuffer() {
        return backBuffer;
    }

    /**
     * Grabs the graphics visible on the canvas from the main buffer using the associated provider
     * @param mainBufferProvider    An object that provides the main buffer (canvas info) for this client instance
     * @return  The graphics of the Canvas
     */
    public Graphics getBufferGraphics(MainBufferProvider mainBufferProvider) {
        image = mainBufferProvider.getImage();
        Graphics back = mainBufferProvider.getImage().getGraphics();
        paintEvent.graphics = back;
        textPaintEvent.graphics = back;
        textPaintEvent.idx = 0;
        eventManager.processEvent(paintEvent);
        eventManager.processEvent(textPaintEvent);
        back.dispose();
        back.drawImage(backBuffer, 0, 0, null);
        return back;
    }

    public Component getPanel() {
        return this.panel;
    }

    public void setPanel(Component c) {
        this.panel = c;
    }

    /**
     * Returns the size of the panel that clients should be drawn into. For
     * internal use.
     *
     * @return The client panel size.
     */
    public Dimension getPanelSize() {
        for (BotLiteInterface bot : Application.getBots()) {
            if (bot != null) {
                if (((BotLite) bot).getClient().getClass().getClassLoader() == this.getClient().getClass().getClassLoader()) {
                    return this.getPanel().getSize();
                }
            }
        }
        return null;
    }

    /**
     * Sets an account for the RuneLite (Bot) instance
     * @param name  The name of the account
     * @return  If the account existed already
     */
    public boolean setAccount(final String name) {
        if (name != null) {
            for (String s : AccountManager.getAccountNames()) {
                if (s.toLowerCase().equals(name.toLowerCase())) {
                    account = name;
                    return true;
                }
            }
        }
        account = null;
        return false;
    }

    /**
     * Gets the canvas object while checking to make sure we don't do this before it has actually
     * loaded
     * @return  The Canvas if the client is loaded otherwise null
     */
    public Canvas getCanvas() {
        if (client == null) {
            return null;
        }
        if (client.getCanvas() == null) {
            return null;
        }
        if (canvas == null || !canvas.isValid()) {
            canvas = new Canvas(client.getCanvas());
            return canvas;
        }
        return canvas;
    }

    public java.awt.Canvas getLoader() {
        return this.getClient().getCanvas();
    }

    /**
     * Assigns this instance of the RuneLite (Bot) a method context for calling bot api methods
     * as well as assigns bank constants here.
     */
    public void setMethodContext() {
        methods = new MethodContext(this);
        methods.bank.assignConstants();
    }

    /**
     * Stops and shuts down the current bot instance
     */
    public void shutdown() {
        java.awt.Canvas loader = getLoader();
        if (loader != null) {
            loader.setVisible(false);
        }
        eventManager.killThread(false);
        sh.stopScript();
        psh.stopScript();
        kill_passive = true;
    }

    public BotLite getInstance() {
        return this;
    }

    public BotLite getInjectorInstance() {
        return injector.getInstance(BotLite.class);
    }


    /**
     * The actual method associated with initializing the client-related data. Such as creating the client sizing and
     * binding the plethora of handlers, listeners, and managers to this particular RuneLite instance
     * (outside the injector binding)
     *
     * @param  startClientBare  Whether to launch the client without any additional initialization settings or not
     * @throws Exception        Any exception the client, bot, or RuneLite might throw.
     */
    public void init(boolean startClientBare) throws Exception {
        if (startClientBare) {
            this.bareStart();
        }
        else {
            this.start();
        }
        // Register the bot sidebar panel after the UI is fully initialized.
        // We delay this to ensure ClientUI.sidebar is created (avoids the NPE
        // that occurs when plugins call addNavigation during early startup).
        initBotPanel();
    }

    /**
     * Manually initializes and registers the Bot sidebar panel with RuneLite's toolbar.
     * This bypasses RuneLite's plugin discovery timing issues where the sidebar
     * isn't ready when plugins call addNavigation() during startUp().
     */
    private void initBotPanel() {
        SwingUtilities.invokeLater(() -> {
            try {
                BotLite bot = getInjectorInstance();
                BotPanel panel = injector.getInstance(BotPanel.class);

                AccountPanel accountPanel = new AccountPanel(bot);
                ScriptPanel scriptPanel = new ScriptPanel(bot);
                DebugPanel debugPanel = new DebugPanel(bot);

                panel.associateBot(accountPanel, scriptPanel, debugPanel);

                InputStream iconStream = BotPlugin.class.getResourceAsStream("rsb.png");
                BufferedImage icon;
                if (iconStream != null) {
                    Image img = ImageIO.read(iconStream);
                    icon = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    Graphics g = icon.getGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                } else {
                    log.warn("Could not load rsb.png icon for bot panel");
                    icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
                }

                NavigationButton navButton = NavigationButton.builder()
                        .tooltip("Bot Interface")
                        .icon(icon)
                        .priority(10)
                        .panel(panel)
                        .build();

                ClientToolbar toolbar = injector.getInstance(ClientToolbar.class);
                toolbar.addNavigation(navButton);
                log.info("Bot panel registered in sidebar");
            } catch (Exception e) {
                log.error("Failed to initialize bot panel — retrying in 3s", e);
                // Retry once after a delay in case the sidebar isn't ready yet
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                initBotPanelRetry();
                            } catch (Exception ex) {
                                log.error("Bot panel retry also failed", ex);
                            }
                        });
                    } catch (InterruptedException ignored) {}
                }, "BotPanel-Retry").start();
            }
        });
    }

    private void initBotPanelRetry() {
        try {
            BotLite bot = getInjectorInstance();
            BotPanel panel = injector.getInstance(BotPanel.class);
            AccountPanel accountPanel = new AccountPanel(bot);
            ScriptPanel scriptPanel = new ScriptPanel(bot);
            DebugPanel debugPanel = new DebugPanel(bot);
            panel.associateBot(accountPanel, scriptPanel, debugPanel);

            BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            NavigationButton navButton = NavigationButton.builder()
                    .tooltip("Bot Interface")
                    .icon(icon)
                    .priority(10)
                    .panel(panel)
                    .build();

            ClientToolbar toolbar = injector.getInstance(ClientToolbar.class);
            toolbar.addNavigation(navButton);
            log.info("Bot panel registered in sidebar (retry succeeded)");
        } catch (Exception e) {
            log.error("Bot panel registration failed on retry", e);
        }
    }

    public BotLite() throws Exception {
        im = new InputManager(this);
        psh = new PassiveScriptHandler(this);
        eventManager = new EventManager();
        sh = new ScriptHandler(this);
        bh = new BreakHandler(this);
        paintEvent = new PaintEvent();
        textPaintEvent = new TextPaintEvent();
        listeners = new TreeMap<>();
        eventManager.start();
        Executors.newSingleThreadScheduledExecutor().submit(() -> {
            while(this.getClient() == null){}
            setMethodContext();
            sh.init();
            if (getPanelSize() != null) {
                final Dimension size = getPanelSize();
                backBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            }
        });
    }

    public void runScript(String account, String scriptName) {
        try {
            getInjectorInstance().setAccount(account);
        } catch (Exception e) {
            log.warn("Could not set account '{}', continuing without account: {}", account, e.getMessage());
        }
        ScriptSelector ss = new ScriptSelector(getInjectorInstance());
        ss.load();
        ScriptDefinition def = ss.getScripts().stream().filter(x -> x.name.replace(" ", "").equals(scriptName)).findFirst().get();
        try {
            getInjectorInstance().getScriptHandler().runScript(def.source.load(def));
        } catch (Exception e) {
            log.error("Failed to run script: {}", scriptName, e);
        }
    }

    public void stopScript() {
        sh.stopScript();
    }

}
