package rsb.plugin.base;

import rsb.botLauncher.Application;
import rsb.botLauncher.RuneLite;
import rsb.internal.globval.GlobalConfiguration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralPanel extends JPanel {

    private static GeneralPanel generalPanel;
    JButton addBotButton = new JButton("Add bot");
    JButton openScriptsFolderButton = new JButton("Scripts Folder");

    public static GeneralPanel getInstance() {
        if (generalPanel == null) {
            generalPanel = new GeneralPanel();
        }
        return generalPanel;
    }

    public GeneralPanel() {
        super();
        generalPanel = this;
        init();
    }

    public void addBotButtonAction() {
        Application.addBot(true);
        Application.setBot(Application.getBots().length - 1);
    }


    private void init() {
        addBotButton.addActionListener(e -> addBotButtonAction());
        this.add(addBotButton);
        openScriptsFolderButton.addActionListener(e -> openScriptsFolderPerformed());
        this.add(openScriptsFolderButton);
    }

    /**
     * Opens the scripts folder in the default file explorer
     */
    private void openScriptsFolderPerformed() {
        String folderPath = GlobalConfiguration.Paths.getScriptsPrecompiledDirectory();
        try {
            switch (GlobalConfiguration.getCurrentOperatingSystem()) {
                case WINDOWS:
                    Runtime.getRuntime().exec("explorer.exe " + folderPath);
                    break;
                case LINUX:
                    Runtime.getRuntime().exec("xdg-open " + folderPath);
                    break;
                case MAC:
                    Runtime.getRuntime().exec("open " + folderPath);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
