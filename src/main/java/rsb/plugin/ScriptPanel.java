package rsb.plugin;

import net.runelite.client.ui.PluginPanel;
import rsb.botLauncher.RuneLite;
import rsb.internal.ScriptHandler;
import rsb.internal.globval.GlobalConfiguration;
import rsb.script.Script;
import rsb.service.ScriptDefinition;
import rsb.service.ServiceException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;


public class ScriptPanel extends PluginPanel {
	private RuneLite bot;
	private JScrollPane scrollPane1;
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
	 * Opens the scripts folder in the default file explorer
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
	 * Opens the scripts folder in the default file explorer
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
		scriptSelector.accounts = scriptSelector.getAccounts();
		//Make a search area
		scriptSelector.getSearch();
		scriptSelector.load();
		scriptSelector.buttonStart = new JButton();
		scriptSelector.buttonPause = new JButton();
		scriptSelector.buttonStop = new JButton();
		scriptSelector.buttonReload = new JButton();
		buttonScriptsFolder = new JButton();
		buttonForums = new JButton();

		//======== this ========
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
				, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
				, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
				getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
		) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

		//======== scripts scroll pane ========
		scriptsSelectionScrollPane.setViewportView(scriptSelector.table);

		//---- buttonStart ----
		scriptSelector.buttonStart.setText("Start");
		scriptSelector.buttonStart.addActionListener(scriptSelector::buttonStartActionPerformed);

		//---- buttonPause ----
		scriptSelector.buttonPause.setText("Pause");
		scriptSelector.buttonPause.addActionListener(scriptSelector::buttonPauseActionPerformed);

		//---- buttonStop ----
		scriptSelector.buttonStop.setText("Stop");
		scriptSelector.buttonStop.addActionListener(scriptSelector::buttonStopActionPerformed);

		//---- buttonReload ----
		scriptSelector.buttonReload.setText("Reload");
		scriptSelector.buttonReload.addActionListener(scriptSelector::buttonReloadActionPerformed);

		//---- buttonScriptsFolder ----
		buttonScriptsFolder.setText("Scripts Folder");
		buttonScriptsFolder.addActionListener(e -> openScriptsFolderPerformed(e));

		//---- buttonForums ----
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
												.addComponent(scriptSelector.accounts, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addComponent(scriptSelector.buttonStart, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(scriptSelector.buttonPause, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addGap(73, 73, 73)
												.addComponent(scriptSelector.buttonStop, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addGap(73, 73, 73)
												.addComponent(scriptSelector.buttonReload, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addGap(10, 10, 10)
												.addComponent(buttonScriptsFolder, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
										.addGap(10, 10, 10))
								.addContainerGap(30, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(scriptsSelectionScrollPane, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
								.addGap(28, 28, 28)
								.addComponent(scriptSelector.accounts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(57, 57, 57)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(scriptSelector.buttonStart, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addComponent(scriptSelector.buttonPause, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
								.addGap(28, 28, 28)
								.addComponent(scriptSelector.buttonStop, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 60, Short.MAX_VALUE)
								.addComponent(scriptSelector.buttonReload, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 60, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(buttonScriptsFolder, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(30, Short.MAX_VALUE))


		);
	}

	/**
	 * Redefines the list of accounts in the dropdown list with an updated set of values
	 * by reassigning the model
	 */
	public void updateAccountList() {
		scriptSelector.accounts.setModel(scriptSelector.getAccounts().getModel());
	}
}
