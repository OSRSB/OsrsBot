package rsb.internal.input;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.input.KeyListener;
import rsb.methods.MethodContext;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;


@Slf4j
public class VirtualKeyboard implements KeyListener {

    private MethodContext methods;

    public VirtualKeyboard(MethodContext ctx) {
        this.methods = ctx;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void sendEvent(KeyEvent e){

        if (e.getID() == KeyEvent.KEY_TYPED) {
            keyTyped(e);
        }
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        }
        if (e.getID() == KeyEvent.KEY_RELEASED) {
            keyReleased(e);
        }
        EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        eventQueue.postEvent(new FocusEvent( ((Applet) methods.client).getComponent(0), FocusEvent.FOCUS_GAINED));
        eventQueue.postEvent(e);
    }


}


