package rsb.internal.naturalmouse;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import rsb.internal.InputManager;
import rsb.methods.MethodContext;

import java.awt.*;

public class RSBSystemCalls implements SystemCalls {

    InputManager inputManager;
    public RSBSystemCalls(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    @Override
    public Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * <p>Moves the mouse to specified pixel using the provided Robot.</p>
     *
     * <p>It seems there is a certain delay, measurable in less than milliseconds,
     * before the mouse actually ends up on the requested pixel when using a Robot class.
     * this usually isn't a problem, but when we ask the mouse position right after this call,
     * there's extremely low but real chance we get wrong information back. I didn't add sleep
     * here as it would cause overhead to sleep always, even when we don't instantly use
     * the mouse position, but just acknowledged the issue with this warning.
     * (Use fast unrestricted loop of Robot movement and checking the position after every move to invoke the issue.)</p>
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    @Override
    public void setMousePosition(int x, int y) {
        inputManager.hopMouse(x, y);
    }
}
