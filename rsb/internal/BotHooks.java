/**
 * Author: GigiaJ
 *
 * This class is the same as the Hooks.java file in RuneLite, but overrides the draw method to allow us to utilize it
 * without consistently editing RuneLite and allowing us to use it to draw our own objects
 *
 */
package net.runelite.client.rsb.internal;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.inject.Singleton;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MainBufferProvider;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.callback.Hooks;
import net.runelite.client.rsb.gui.BotGUI;

@Singleton
@Slf4j
public class BotHooks extends Hooks {

    private LinkedHashMap SUPERCLASS_MAP = new LinkedHashMap();

    /*
     * Prepared for later use if needed Create a string constant for superclass
     * access in hooks Then place it in the superclass map and execute the
     * method/field similarly to how GETGRAPHICS is
     */
    private final String GETGRAPHICS = "getGraphics";

    {
        SUPERCLASS_MAP.put(GETGRAPHICS, null);
    }

    Injector injector = RuneLite.getInjector();
    RuneLite bot = injector.getInstance(RuneLite.class);


    @Override
    public void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y) {
        try {


            //Preferably initialize the value of bot with the current bot on screen
           // IE Application.getBot or similar manner.



            if (graphics == null) {
                return;
            }
            // We only want do do this once because this would MASSIVELY affect performance.
            if (SUPERCLASS_MAP.get(SUPERCLASS_MAP.keySet().iterator().next()) == null) {

                for (Method method : this.getClass().getSuperclass().getDeclaredMethods()) {
                    Iterator itr = SUPERCLASS_MAP.keySet().iterator();
                    while (itr.hasNext()) {
                        String name = (String) itr.next();
                        if (method.getName().equals(name)) {
                            method.setAccessible(true);
                            SUPERCLASS_MAP.replace(name, null, method);
                        }
                    }
                }
                for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {
                    Iterator itr = SUPERCLASS_MAP.keySet().iterator();
                    while (itr.hasNext()) {
                        String name = (String) itr.next();
                        if (field.getName().equals(name)) {
                            field.setAccessible(true);
                            SUPERCLASS_MAP.replace(name, null, field);
                        }
                    }
                }
            }

            //We then simply invoke the method as needed here. It should perform similar to how it does in RuneLite
            //final Graphics2D graphics2d = (Graphics2D) ((Method) SUPERCLASS_MAP.get(GETGRAPHICS)).invoke(this,
             //       mainBufferProvider);

            final Graphics2D g2d = (Graphics2D) bot.getCanvas().getGraphics(bot, mainBufferProvider);

            // Draw bot overlays
            BotGUI.paintOverlays(bot.getClient(), g2d);

            Image image = mainBufferProvider.getImage();

            // Draw the image onto the game canvas
            graphics.drawImage(image, 0, 0, bot.getCanvas());


            // finalImage is backed by the client buffer which will change soon. make a copy
            // so that callbacks can safely use it later from threads.
            // drawManager.processDrawComplete(() -> copy(image));

        } catch (Exception e) {
            e.printStackTrace();
            //log.warn("Bot hooks failed to paint properly");
        }
    }

}