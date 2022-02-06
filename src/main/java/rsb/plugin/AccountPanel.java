package rsb.plugin;

import rsb.botLauncher.RuneLite;
import rsb.internal.ScriptHandler;
import rsb.internal.listener.ScriptListener;
import rsb.script.Script;
import net.runelite.client.ui.PluginPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A class that acts as effectively a single button to launch the account manager interface
 */
public class AccountPanel extends PluginPanel implements ScriptListener {

	private JScrollPane scrollPane1;
	private JTable table1;
	private JButton buttonAccounts;
	private JButton buttonScripts;
	private RuneLite bot;

	/**
	 * Creates an account panel bound to a singleton of a bot
	 * TODO: Change this to not be hard bound, but rather iterable through a list of active clients
	 * @param bot	the bot singleton to associate with this panel
	 */
	public AccountPanel(RuneLite bot) {
		initComponents();
		this.bot = bot;
		bot.getScriptHandler().init();
	}

	/**
	 * Assigns the action for the accounts button action
	 * @param e	the action event to assign
	 */
	private void buttonAccountActionPerformed(ActionEvent e) {
		AccountManager.getInstance().showGUI();
	}

	/**
	 * Initializes the components for the panel
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
	 * Handles any task necessary if a script has been started
	 *
	 * @param handler	the script handler
	 * @param script	the script to start
	 */
	public void scriptStarted(final ScriptHandler handler, Script script) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				RuneLite bot = handler.getBot();
			}
		});
	}


	/**
	 * Handles any task necessary if a script has been stopped
	 *
	 * @param handler	the script handler
	 * @param script	the script to stop
	 */
	public void scriptStopped(ScriptHandler handler, Script script) {

	}

	/**
	 * Handles any task necessary on a script being resumed
	 *
	 * @param handler	the script handler
	 * @param script	the script to resume
	 */
	public void scriptResumed(ScriptHandler handler, Script script) {

	}

	/**
	 * Handles any task necessary on a script being paused
	 *
	 * @param handler	the script handler
	 * @param script	the script to pause
	 */
	public void scriptPaused(ScriptHandler handler, Script script) {

	}


	/**
	 * Handles any task necessary if the input has changed
	 *
	 * @param bot		the bot instance to check
	 * @param mask		the mask to check for
	 */
	public void inputChanged(RuneLite bot, int mask) {

	}
}
