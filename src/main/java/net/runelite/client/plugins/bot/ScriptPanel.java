package net.runelite.client.plugins.bot;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.internal.globval.GlobalConfiguration;
import net.runelite.rsb.plugin.ScriptSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScriptPanel extends PluginPanel {
	private BotLite bot;
	private JScrollPane scrollPane1;
	private JScrollPane scriptsSelectionScrollPane;
	private JTable scriptsTable;
	private JComboBox comboBoxAccounts;
	private JButton buttonStart;
	private JButton buttonPause;
	private JButton buttonStop;
	private MaterialTab buttonScriptsFolder;
	private JButton buttonForums;
	private ScriptSelector scriptSelector;
	private MaterialTabGroup scriptPanelToolbar;

	public ScriptPanel(BotLite bot) {
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
		buttonForums = new JButton();

		scriptPanelToolbar = new MaterialTabGroup();
		scriptPanelToolbar.setLayout(new GridLayout(1, 5, 5, 5));

		//======== this ========
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
				, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
				, new Font ("D\u0069alog" , Font .BOLD ,12 ), Color. red) ,
				getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
		) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

		//======== scripts scroll pane ========
		scriptsSelectionScrollPane.setViewportView(scriptSelector.table);

		//---- buttonStart ----
		//scriptSelector.buttonStart.setText("Start");
		//scriptSelector.buttonStart.addActionListener(scriptSelector::buttonStartActionPerformed);
		final BufferedImage startIcon = ImageUtil.loadImageResource(getClass(), "start.png");
		scriptSelector.buttonStart = new MaterialTab(new ImageIcon(startIcon.getScaledInstance(24, 24, 5)), scriptPanelToolbar, null);
		scriptSelector.buttonStart.setToolTipText("Start selected script");
		scriptSelector.buttonStart.setSize(new Dimension(28, 28));
		scriptSelector.buttonStart.setMinimumSize(new Dimension(0, 28));
		scriptSelector.buttonStart.setEnabled(false);
		scriptSelector.buttonStart.setOpaque(true);
		scriptSelector.buttonStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				scriptSelector.buttonStartActionPerformed(null);
			}
		});
		scriptPanelToolbar.addTab(scriptSelector.buttonStart);

		//---- buttonPause ----
		//scriptSelector.buttonPause.setText("Pause");
		//scriptSelector.buttonPause.addActionListener(scriptSelector::buttonPauseActionPerformed);
		final BufferedImage pauseIcon = ImageUtil.loadImageResource(getClass(), "pause.png");
		scriptSelector.buttonPause = new MaterialTab(new ImageIcon(pauseIcon.getScaledInstance(20, 20, 5)), scriptPanelToolbar, null);
		scriptSelector.buttonPause.setToolTipText("Pause the active script");
		scriptSelector.buttonPause.setSize(new Dimension(28, 28));
		scriptSelector.buttonPause.setMinimumSize(new Dimension(0, 0));
		scriptSelector.buttonPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				scriptSelector.buttonPauseActionPerformed(null);
			}
		});
		scriptPanelToolbar.addTab(scriptSelector.buttonPause);

		//---- buttonStop ----
		//scriptSelector.buttonStop.setText("Stop");
		//scriptSelector.buttonStop.addActionListener(scriptSelector::buttonStopActionPerformed);
		final BufferedImage stopIcon = ImageUtil.loadImageResource(getClass(), "stop.png");
		scriptSelector.buttonStop = new MaterialTab(new ImageIcon(stopIcon.getScaledInstance(20, 20, 5)), scriptPanelToolbar, null);
		scriptSelector.buttonStop.setToolTipText("Stop running the active script");
		scriptSelector.buttonStop.setSize(new Dimension(28, 28));
		scriptSelector.buttonStop.setMinimumSize(new Dimension(0, 28));
		scriptSelector.buttonStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				scriptSelector.buttonStopActionPerformed(null);
			}
		});
		scriptPanelToolbar.addTab(scriptSelector.buttonStop);

		//---- buttonReload ----
		final BufferedImage iconImage = ImageUtil.loadImageResource(getClass(), "reload.png");
		scriptSelector.buttonReload = new MaterialTab(new ImageIcon(iconImage.getScaledInstance(20, 20, 5)), scriptPanelToolbar, null);
		scriptSelector.buttonReload.setToolTipText("Reload Plugins");
		scriptSelector.buttonReload.setSize(new Dimension(28, 28));
		scriptSelector.buttonReload.setMinimumSize(new Dimension(0, 28));
		scriptSelector.buttonReload.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				scriptSelector.buttonReloadActionPerformed();
			}
		});
		scriptPanelToolbar.addTab(scriptSelector.buttonReload);

		//---- buttonScriptsFolder ----
		final BufferedImage folder = ImageUtil.loadImageResource(getClass(), "open-folder.png");
		buttonScriptsFolder = new MaterialTab(new ImageIcon(folder.getScaledInstance(20, 20, 5)), scriptPanelToolbar, null);
		buttonScriptsFolder.setToolTipText("Open scripts folder");
		buttonScriptsFolder.setOpaque(true);
		buttonScriptsFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				openScriptsFolderPerformed(null);
			}
		});
		scriptPanelToolbar.addTab(buttonScriptsFolder);

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
						.addComponent(scriptPanelToolbar, 0, 240, Short.MAX_VALUE)
						.addGap(10, 10, 10)
						.addComponent(scriptsSelectionScrollPane, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addGroup(layout.createSequentialGroup()
												.addGap(47, 47, 47)
												.addComponent(scriptSelector.accounts, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))

										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addGap(18, 18, 18)
												.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
										.addGap(10, 10, 10))
								.addContainerGap(30, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(scriptPanelToolbar, 28, 40, 40)
								.addComponent(scriptsSelectionScrollPane, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(scriptSelector.accounts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 10, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(buttonForums, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(10, Short.MAX_VALUE))


		);
	}

	/**
	 * Redefines the list of accounts in the dropdown list with an updated set of values
	 * by reassigning the model
	 * TODO: Add this to an event listener
	 */
	public void updateAccountList() {
		scriptSelector.accounts.setModel(scriptSelector.getAccounts().getModel());
	}
}
