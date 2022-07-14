package net.runelite.rsb.botLauncher;

public interface RuneLiteInterface {

    void runScript(String account, String scriptName);

    // void launch(String[]s) throws Exception;

    void init(boolean startClientBare) throws Exception;

}
