package rsb.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

import java.awt.*;

public class SetForegroundWindowUtil {
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);

        int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

        int SetForegroundWindow(Pointer hWnd);

        Pointer GetForegroundWindow();
    }

    public static boolean setForegroundWindowByName(final String windowName,
                                                    final boolean starting) {
        final User32 user32 = User32.INSTANCE;
        return user32.EnumWindows(new User32.WNDENUMPROC() {

            @Override
            public boolean callback(Pointer hWnd, Pointer arg) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);
                // if (wText.contains(WINDOW_TEXT_TO_FIND)) {
                if (starting) {
                    if (wText.startsWith(windowName)) {
                        user32.SetForegroundWindow(hWnd);
                        return false;
                    }
                } else {
                    if (wText.contains(windowName)) {
                        user32.SetForegroundWindow(hWnd);
                        return false;
                    }
                }
                return true;
            }
        }, null);
    }



}
