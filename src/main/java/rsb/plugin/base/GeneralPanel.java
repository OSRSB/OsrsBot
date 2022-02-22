package rsb.plugin.base;

import rsb.botLauncher.Application;
import rsb.botLauncher.RuneLite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralPanel extends JPanel {

    private static GeneralPanel generalPanel;
    JButton addBotButton = new JButton("Add bot");
    JButton scriptsFolderButton = new JButton("Scripts Folder");

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
        RuneLite bot = new RuneLite();
        Application.addBot(bot);
        Application.setBot(Application.getBots().length - 1);
    }


    private void init() {
        addBotButton.addActionListener(e -> addBotButtonAction());
        this.add(addBotButton);
        generalPanel.add(scriptsFolderButton);
    }
}
