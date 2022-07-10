package rsb.botLauncher;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.runelite.api.Client;
import net.runelite.api.MainBufferProvider;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface RuneLiteInterface {
    void launch(OptionParser parser, ArgumentAcceptingOptionSpec<?>[] optionSpecs, OptionSet options) throws Exception;

    Graphics getBufferGraphics(MainBufferProvider mainBufferProvider);

    Client getClient();

    Component getPanel();

    BufferedImage getBackBuffer();

    void init(boolean startClientBare) throws Exception;

    /**
     * Our set of options corresponding to the command-line arguments that should be parsed.
     * The values assigned are their positions within the relating ArgumentAcceptingOptionSpec array
     */
    enum Options {
        SESSION_FILE(0),CONFIG_FILE(1), UPDATE_MODE(2), PROXY_INFO(3);

        private int index;

        Options(int arrayIndex) {
            this.index = arrayIndex;
        }

        public int getIndex() {
            return index;
        }
    }
}
