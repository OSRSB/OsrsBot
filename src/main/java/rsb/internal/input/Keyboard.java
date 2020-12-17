package rsb.internal.input;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.input.KeyListener;
import rsb.internal.input.Focus;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

@Slf4j
public class Keyboard extends Focus implements KeyListener {
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */

    public void keyPressed(KeyEvent e) {
        log.debug("TEST");
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    public void keyReleased(KeyEvent e) {

    }

    public void _focusGained(FocusEvent e) {

    }

    @Override
    public void _focusLost(FocusEvent e) {

    }
}
