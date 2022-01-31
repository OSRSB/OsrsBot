package rsb.plugin;

import lombok.extern.slf4j.Slf4j;
import rsb.util.AccountStore;
import rsb.internal.globval.GlobalConfiguration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author GigiaJ
 */
@SuppressWarnings("serial")
@Slf4j
public class AccountManager extends JDialog implements ActionListener {

	private static final String FILE_NAME = GlobalConfiguration.Paths.getAccountsFile();

	private static final String[] RANDOM_REWARDS = {"Cash", "Runes", "Coal", "Essence", "Ore", "Bars", "Gems", "Herbs",
			"Seeds", "Charms", "Surprise", "Emote", "Costume", "Attack",
			"Defence", "Strength", "Constitution", "Range", "Prayer", "Magic",
			"Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking",
			"Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving",
			"Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering"};

	private static final String[] VALID_KEYS = {"pin", "reward", "member", "take_breaks"};

	private static final AccountStore accountStore = new AccountStore(new File(FILE_NAME));

	static {
		accountStore.setPassword("0000000000000000000000000000000000000000");
		try {
			accountStore.load();
		} catch (IOException ignored) {
			log.debug("Failed to load accounts", ignored);
		}
	}

	private static class RandomRewardEditor extends DefaultCellEditor {
		public RandomRewardEditor() {
			super(new JComboBox(RANDOM_REWARDS));
		}
	}

	private static class PasswordCellEditor extends DefaultCellEditor {
		public PasswordCellEditor() {
			super(new JPasswordField());
		}
	}

	private static class PasswordCellRenderer extends DefaultTableCellRenderer {
		@Override
		protected void setValue(Object value) {
			if (value == null) {
				setText("<none>");
			} else {
				String str = value.toString();
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < str.length(); ++i) {
					b.append("*");
				}
				setText(b.toString());
			}
		}
	}

	private class TableSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent evt) {
			int row = table.getSelectedRow();
			if (!evt.getValueIsAdjusting()) {
				removeButton.setEnabled(row >= 0 && row < table.getRowCount());
			}
		}
	}

	private class AccountTableModel extends AbstractTableModel {
		public int getRowCount() {
			return accountStore.list().size();
		}

		public int getColumnCount() {
			return VALID_KEYS.length + 2;
		}

		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return userForRow(row);
			} else if (column == 1) {
				return accountStore.get(userForRow(row)).getPassword();
			} else {
				AccountStore.Account acc = accountStore.get(userForRow(row));
				if (acc != null) {
					String str = acc.getAttribute(VALID_KEYS[column - 2]);
					if (str == null || str.isEmpty()) {
						return null;
					}
					if (getColumnClass(column) == Boolean.class) {
						return Boolean.parseBoolean(str);
					} else if (getColumnClass(column) == Integer.class) {
						return Integer.parseInt(str);
					} else {
						return str;
					}
				}
			}
			return null;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "Username";
			} else if (column == 1) {
				return "Password";
			}
			String str = VALID_KEYS[column - 2];
			StringBuilder b = new StringBuilder();
			boolean space = true;
			for (char c : str.toCharArray()) {
				if (c == '_') {
					c = ' ';
				}
				b.append(space ? Character.toUpperCase(c) : c);
				space = c == ' ';
			}
			return b.toString();
		}

		@Override
		public Class<?> getColumnClass(int column) {
			if (getColumnName(column).equals("Member")) {
				return Boolean.class;
			}
			if (getColumnName(column).equals("Take Breaks")) {
				return Boolean.class;
			}
			return Object.class;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column > 0;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			AccountStore.Account acc = accountStore.get(userForRow(row));
			if (acc == null) {
				return;
			}
			if (column == 1) {
				acc.setPassword(String.valueOf(value));
			} else {
				acc.setAttribute(getColumnName(column).toLowerCase().replace(' ', '_'), String.valueOf(value));
			}
			fireTableCellUpdated(row, column);
		}

		public String userForRow(int row) {
			Iterator<AccountStore.Account> it = accountStore.list().iterator();
			for (int k = 0; it.hasNext() && k < row; k++) {
				it.next();
			}
			if (it.hasNext()) {
				return it.next().getUsername();
			}
			return null;
		}
	}

	private JTable table;
	private JButton removeButton;

	public AccountManager() {
		super(Frame.getFrames()[0], "Account Manager", true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			String label = ((JButton) e.getSource()).getText();
			switch (label) {
				case "Done":
					try {
						accountStore.save();
						Botplugin.getScriptPanel().updateAccountList();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						log.info("Failed to save accounts...  Please report this.");
					}
					dispose();

					break;
				case "Add": {
					String str = JOptionPane.showInputDialog(getParent(), "Enter the account username.", "New Account", JOptionPane.QUESTION_MESSAGE);
					if (str == null || str.isEmpty()) {
						return;
					}
					accountStore.add(new AccountStore.Account(str));
					accountStore.get(str).setAttribute("reward", RANDOM_REWARDS[0]);
					int row = table.getRowCount();
					((AccountTableModel) table.getModel()).fireTableRowsInserted(row, row);
					break;
				}
				case "Remove": {
					int row = table.getSelectedRow();
					String user = ((AccountTableModel) table.getModel()).userForRow(row);
					if (user != null) {
						accountStore.remove(user);
						((AccountTableModel) table.getModel()).fireTableRowsDeleted(row, row);
					}
					break;
				}
			}
		}
	}

	/**
	 * Creates and displays the main GUI This GUI has the list and the main	 * buttons
	 */
	public void showGUI() {
		JScrollPane scrollPane = new JScrollPane();
		table = new JTable(new AccountTableModel());
		JPanel bar = new JPanel();
		removeButton = new JButton();
		JButton newButton = new JButton();
		JButton doneButton = new JButton();
		setTitle("Account Manager");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(5, 5));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
		TableColumnModel cm = table.getColumnModel();
		cm.getColumn(cm.getColumnIndex("Password")).setCellRenderer(new PasswordCellRenderer());
		cm.getColumn(cm.getColumnIndex("Password")).setCellEditor(new PasswordCellEditor());
		cm.getColumn(cm.getColumnIndex("Pin")).setCellRenderer(new PasswordCellRenderer());
		cm.getColumn(cm.getColumnIndex("Pin")).setCellEditor(new PasswordCellEditor());
		cm.getColumn(cm.getColumnIndex("Reward")).setCellEditor(new RandomRewardEditor());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		GridBagLayout gbl = new GridBagLayout();
		bar.setLayout(gbl);
		gbl.rowHeights = new int[]{0, 0};
		gbl.rowWeights = new double[]{0.0, 1.0E-4};
		newButton.setText("Add");
		bar.add(newButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));
		removeButton.setText("Remove");
		bar.add(removeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));
		doneButton.setText("Done");
		bar.add(doneButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
		newButton.addActionListener(this);
		doneButton.addActionListener(this);
		removeButton.addActionListener(this);
		contentPane.add(bar, BorderLayout.SOUTH);
		int row = table.getSelectedRow();
		removeButton.setEnabled(row >= 0 && row < table.getRowCount());
		table.clearSelection();
		doneButton.requestFocus();
		setPreferredSize(new Dimension(600, 300));
		pack();
		setLocationRelativeTo(getOwner());
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Access the list of names for loaded accounts
	 *
	 * @return Array of the names.
	 */
	public static String[] getAccountNames() {
		try {
			List<String> theList = new ArrayList<String>();
			Collection<AccountStore.Account> accountCollection = AccountManager.accountStore.list();
			for (AccountStore.Account anAccountCollection : accountCollection) {
				AccountStore.Account account = anAccountCollection;
				theList.add(account.getUsername());
			}
			return theList.toArray(new String[theList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AccountManager getInstance() {
		return new AccountManager();
	}

	/**
	 * Access the account password of the given string
	 *
	 * @param name The name of the account
	 * @return Password or an empty string
	 */
	public static String getPassword(final String name) {
		AccountStore.Account values = AccountManager.accountStore.get(name);
		String pass = values.getPassword();
		if (pass == null) {
			pass = "";
		}
		return pass;
	}

	/**
	 * Access the account pin of the given string
	 *
	 * @param name The name of the account
	 * @return Pin or an empty string
	 */
	public static String getPin(final String name) {
		AccountStore.Account values = AccountManager.accountStore.get(name);
		String pin = values.getAttribute("pin");
		if (pin == null) {
			pin = "-1";
		}
		return pin;
	}

	/**
	 * Access the account desired reward of the given string
	 *
	 * @param name The name of the account
	 * @return The desired reward
	 */
	public static String getReward(final String name) {
		AccountStore.Account values = AccountManager.accountStore.get(name);
		String reward = values.getAttribute("reward");
		if (reward == null) {
			return "Cash";
		}
		return reward;
	}

	/**
	 * Access the account state of the given string
	 *
	 * @param name Name of the account
	 * @return true if the account is member, false if it isn't
	 */
	public static boolean isMember(final String name) {
		AccountStore.Account values = AccountManager.accountStore.get(name);
		String member = values.getAttribute("member");
		return member != null && member.equalsIgnoreCase("true");
	}

	/**
	 * Access the account state of the given string
	 *
	 * @param name Name of the account
	 * @return true if the account is member, false if it isn't
	 */
	public static boolean isTakingBreaks(final String name) {
		AccountStore.Account values = AccountManager.accountStore.get(name);
		String member = values.getAttribute("take_breaks");
		return member != null && member.equalsIgnoreCase("true");
	}

	/**
	 * Check if the string is a valid key
	 *
	 * @param key The key
	 * @return true if the object is supported, false if it isn't
	 */
	@SuppressWarnings("unused")
	private static boolean isValidKey(final String key) {
		for (String check : VALID_KEYS) {
			if (key.equalsIgnoreCase(check)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given string is a valid pin
	 *
	 * @param pin The pin
	 * @return true if the pin is valid, false if it isn't
	 */
	@SuppressWarnings("unused")
	private static boolean isValidPin(final String pin) {
		if (pin.length() == 4) {
			for (int i = 0; i < pin.length(); i++) {
				final char charAt = pin.charAt(i);
				if (charAt < '0' || charAt > '9') {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}