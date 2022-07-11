package rsb.botLauncher;

public interface RuneLiteInterface {

    void runScript(String account, String scriptName);

    void launch(String[]s) throws Exception;

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
