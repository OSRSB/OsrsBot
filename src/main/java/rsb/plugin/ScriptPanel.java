package rsb.plugin;

import rsb.botLauncher.RuneLite;
import rsb.internal.ScriptHandler;
import rsb.script.Script;
import rsb.service.ScriptDefinition;
import rsb.service.ServiceException;

import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;


public class ScriptPanel extends JPanel {
	private RuneLite bot;
	private JScrollPane scrollPane1;
	private ScriptSelector scriptSelector;

	public ScriptPanel(RuneLite bot) {
		this.bot = bot;
		scriptSelector = new ScriptSelector(bot);
		initComponents();
	}

	private void initComponents() {
		scrollPane1 = new JScrollPane();
		scriptSelector.accounts = scriptSelector.getAccounts();
		//Make a search area
		scriptSelector.getSearch();
		scriptSelector.load();
		//buttonStart = scriptSelector.getSubmit();
		scriptSelector.buttonStart = new JButton();
		scriptSelector.buttonPause = new JButton();
		scriptSelector.buttonStop = new JButton();
		scriptSelector.buttonReload = new JButton();

		//======== this ========
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
		, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
		, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
		 getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
		) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(scriptSelector.table);
		}

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
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
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
												.addComponent(scriptSelector.buttonReload, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))

								.addContainerGap(30, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
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
								.addGap(0, 60, Short.MAX_VALUE))

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
