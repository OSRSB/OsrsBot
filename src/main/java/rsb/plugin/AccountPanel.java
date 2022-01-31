package rsb.plugin;

import rsb.botLauncher.RuneLite;
import rsb.internal.ScriptHandler;
import rsb.internal.listener.ScriptListener;
import rsb.script.Script;
import net.runelite.client.ui.PluginPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Mon Apr 06 21:41:40 CEST 2020
 */



/**
 * @author GigiaJ
 */
public class AccountPanel extends PluginPanel implements ScriptListener {

	private JScrollPane scrollPane1;
	private JTable table1;
	private JButton buttonAccounts;
	private JButton buttonScripts;
	private RuneLite bot;

	public AccountPanel(RuneLite bot) {
		initComponents();
		this.bot = bot;
		bot.getScriptHandler().init();
	}

	private void buttonAccountActionPerformed(ActionEvent e) {
		AccountManager.getInstance().showGUI();
	}

	private void buttonScriptsActionPerformed(ActionEvent e) {
		ScriptSelector.getInstance(bot).showGUI();
	}

	/*
	This will likely be modified and added later, feel free to edit it to make it look better
	 */
	private void initComponents() {
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		buttonAccounts = new JButton();
		buttonScripts = new JButton();


		buttonAccounts.setText("View Accounts");
		buttonAccounts.addActionListener(e -> buttonAccountActionPerformed(e));
		add(buttonAccounts);
		buttonAccounts.setBounds(new Rectangle(new Point(15, 375), buttonAccounts.getPreferredSize()));


		/*
		buttonScripts.setText("View Scripts");
		buttonScripts.addActionListener(e -> buttonScriptsActionPerformed(e));
		add(buttonScripts);
		buttonScripts.setBounds(new Rectangle(new Point(120, 375), buttonScripts.getPreferredSize()));
		 */

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}

	}


	/**
	 * @author GigiaJ
	 *
	 * @description Handles any task necessary if a script has been started
	 *
	 * @param handler
	 * @param script
	 */
	public void scriptStarted(final ScriptHandler handler, Script script) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				RuneLite bot = handler.getBot();
			}
		});
	}


	/**
	 * @author GigiaJ
	 *
	 * @description Handles any task necessary if a script has been stopped
	 *
	 * @param handler
	 * @param script
	 */
	public void scriptStopped(ScriptHandler handler, Script script) {

	}

	/**
	 * @author GigiaJ
	 *
	 * @description Handles any task necessary on a script being resumed
	 *
	 * @param handler
	 * @param script
	 */
	public void scriptResumed(ScriptHandler handler, Script script) {

	}

	/**
	 * @author GigiaJ
	 *
	 * @description Handles any task necessary on a script being paused
	 *
	 * @param handler
	 * @param script
	 */
	public void scriptPaused(ScriptHandler handler, Script script) {

	}


	/**
	 * @author GigiaJ
	 *
	 * @description Handles any task necessary if the input has changed
	 *
	 * @param bot
	 * @param mask
	 */
	public void inputChanged(RuneLite bot, int mask) {

	}
}
