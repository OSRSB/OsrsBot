package rsb.plugin;

import rsb.botLauncher.RuneLite;
import rsb.internal.ScriptHandler;
import rsb.internal.globval.GlobalConfiguration;
import rsb.script.Script;

import java.awt.event.*;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;


public class ScriptPanel extends JPanel {
	private RuneLite bot;
	private JScrollPane scriptsSelectionScrollPane;
	private JTable scriptsTable;
	private JComboBox comboBoxAccounts;
	private JButton buttonStart;
	private JButton buttonPause;
	private JButton buttonStop;
	private JButton buttonScriptsFolder;
	private JButton buttonForums;
	private ScriptSelector scriptSelector;

	public ScriptPanel(RuneLite bot) {
		this.bot = bot;
		scriptSelector = new ScriptSelector(bot);
		initComponents();
	}

	/**
	 * @author GigiaJ
	 * @description Sets the action to occur when the pause button is pressed.
	 *
	 * @param e
	 */
	private void buttonPauseActionPerformed(ActionEvent e) {
		ScriptHandler sh = bot.getScriptHandler();
		Map<Integer, Script> running = sh.getRunningScripts();
		if (running.size() > 0) {
			int id = running.keySet().iterator().next();
			sh.pauseScript(id);
			//Swaps the displayed text
			if (buttonPause.getText().equals("Pause")) {
				buttonPause.setText("Play");
			}
			else {
				buttonPause.setText("Pause");
			}
		}
	}

	/**
	 * @author GigiaJ
	 * @description Sets the action to occur when the stop button is pressed.
	 *
	 * @param e
	 */
	private void buttonStopActionPerformed(ActionEvent e) {
		//Sets the value back to Pause
		if (buttonPause.getText().equals("Play")) {
			buttonPause.setText("Pause");
		}
		ScriptHandler sh = bot.getScriptHandler();
		Map<Integer, Script> running = sh.getRunningScripts();
		if (running.size() > 0) {
			int id = running.keySet().iterator().next();
			//Script s = running.get(id);
			//ScriptManifest prop = s.getClass().getAnnotation(ScriptManifest.class);
			//int result = JOptionPane.showConfirmDialog(this, "Would you like to stop the script " + prop.name() + "?", "Script", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			//if (result == JOptionPane.OK_OPTION) {
			sh.stopScript(id);
			//}
		}
	}

	/**
	 * @author dginovker
	 * @description Opens the scripts folder in the default file explorer
	 *
	 * @param e ActionEvent
	 */
	private void openForumsPerformed(ActionEvent e) {
		String forumsURL = GlobalConfiguration.Paths.URLs.FORUMS;
		try {
			switch (GlobalConfiguration.getCurrentOperatingSystem()) {
				case WINDOWS:
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + forumsURL);
					break;
				case LINUX:
					Runtime.getRuntime().exec("xdg-open " + forumsURL);
					break;
				case MAC:
					Runtime.getRuntime().exec("open " + forumsURL);
					break;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @author dginovker
	 * @description Opens the scripts folder in the default file explorer
	 *
	 * @param e ActionEvent
	 */
	private void openScriptsFolderPerformed(ActionEvent e) {
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

	private void initComponents() {
		scriptsSelectionScrollPane = new JScrollPane();
		scriptsTable = scriptSelector.getTable(0, 70, 45, 30);
		comboBoxAccounts = scriptSelector.getAccounts();
		buttonStart = scriptSelector.getSubmit();
		//Make a search area
		scriptSelector.getSearch();
		scriptSelector.load();
		buttonPause = new JButton();
		buttonStop = new JButton();
		buttonScriptsFolder = new JButton();
		buttonForums = new JButton();

		//======== this ========
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
		, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
		, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
		 getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
		) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

		//======== scripts scroll pane ========
		scriptsSelectionScrollPane.setViewportView(scriptsTable);

		//---- buttonPause ----
		buttonPause.setText("Pause");
		buttonPause.addActionListener(e -> buttonPauseActionPerformed(e));

		//---- buttonStop ----
		buttonStop.setText("Stop");
		buttonStop.addActionListener(e -> buttonStopActionPerformed(e));

		//---- openScriptsFolder ----
		buttonScriptsFolder.setText("Scripts Folder");
		buttonScriptsFolder.addActionListener(e -> openScriptsFolderPerformed(e));

		//---- openForums ----
		buttonForums.setText("Forums");
		buttonForums.addActionListener(e -> openForumsPerformed(e));

		assignLayouts();

	}

	/**
	 * Assigns the layouts for the script panel
	 */
	private void assignLayouts() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
						.addComponent(scriptsSelectionScrollPane, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addGroup(layout.createSequentialGroup()
												.addGap(47, 47, 47)
												.addComponent(comboBoxAccounts, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addComponent(buttonStart, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(buttonPause, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addGap(73, 73, 73)
												.addComponent(buttonStop, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addComponent(buttonScriptsFolder, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(30, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(scriptsSelectionScrollPane, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
								.addGap(28, 28, 28)
								.addComponent(comboBoxAccounts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(57, 57, 57)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(buttonStart, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonPause, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
								.addGap(28, 28, 28)
								.addComponent(buttonStop, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGap(28, 28, 28)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(buttonScriptsFolder, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
		);
	}

	/**
	 * Redefines the list of accounts in the dropdown list with an updated set of values
	 * by reassigning the model
	 */
	public void updateAccountList() {
		comboBoxAccounts.setModel(scriptSelector.getAccounts().getModel());
	}
}
