package rsb.plugin;

import rsb.internal.LogFormatter;
import rsb.internal.globval.GlobalConfiguration;
import rsb.util.StringUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Non swing methods are thread safe.
 */
public class LogTextArea extends JList {

	public static final int MAX_ENTRIES = 100;

	public static final Rectangle BOTTOM_OF_WINDOW = new Rectangle(0,
			Integer.MAX_VALUE, 0, 0);

	private static final long serialVersionUID = 0;

	private final LogQueue logQueue = new LogQueue();

	private final LogAreaListModel model = new LogAreaListModel();

	private final Runnable scrollToBottom = new Runnable() {
		public void run() {
			scrollRectToVisible(LogTextArea.BOTTOM_OF_WINDOW);
		}
	};

	private static final Formatter formatter = new Formatter() {
		private final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"hh:mm:ss");

		@Override
		public String format(final LogRecord record) {
			final String[] className = record.getLoggerName().split("\\.");
			final String name = className[className.length - 1];
			final int maxLen = 16;
			final String append = "...";

			return String.format(
					"[%s] %-" + maxLen + "s %s %s",
					dateFormat.format(record.getMillis()),
					name.length() > maxLen ? name.substring(0,
							maxLen - append.length())
							+ append : name, record.getMessage(),
					StringUtil.throwableToString(record.getThrown()));
		}
	};

	private static final Formatter copyPasteFormatter = new LogFormatter(false);

	public LogTextArea() {
		setModel(model);
		setCellRenderer(new Renderer());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if (GlobalConfiguration.getCurrentOperatingSystem() == GlobalConfiguration.OperatingSystem.MAC) {
			setFont(new Font("Monaco", Font.PLAIN, 10));
		} else {
			setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		}

		new Thread(logQueue, "LogGuiQueue").start();
	}

	/**
	 * Logs a new entry to be shown in the list. Thread safe.
	 *
	 * @param logRecord The entry.
	 */
	public void log(LogRecord logRecord) {
		logQueue.queue(new WrappedLogRecord(logRecord));
	}

	private class LogAreaListModel extends AbstractListModel {
		private static final long serialVersionUID = 0;

		private List<WrappedLogRecord> records = new ArrayList<WrappedLogRecord>(
				LogTextArea.MAX_ENTRIES);

		public void addAllElements(List<WrappedLogRecord> obj) {
			records.addAll(obj);
			if (getSize() > LogTextArea.MAX_ENTRIES) {
				records = records.subList(
						(getSize() - LogTextArea.MAX_ENTRIES), getSize());

				fireContentsChanged(this, 0, (getSize() - 1));
			} else {
				fireIntervalAdded(this, (getSize() - 1), (getSize() - 1));
			}
		}

		public Object getElementAt(final int index) {
			return records.get(index);
		}

		public int getSize() {
			return records.size();
		}

	}

	/**
	 * Flushes every #FLUSH_RATE (miliseconds)
	 */
	private class LogQueue implements Runnable {

		public static final int FLUSH_RATE = 1000;

		private final Object lock = new Object();

		private List<WrappedLogRecord> queue = new ArrayList<WrappedLogRecord>(
				100);

		public void queue(final WrappedLogRecord record) {
			synchronized (lock) {
				queue.add(record);
			}
		}

		public void run() {
			while (true) {
				List<WrappedLogRecord> toFlush = null;

				synchronized (lock) {
					if (queue.size() != 0) {
						toFlush = new ArrayList<WrappedLogRecord>(queue);
						queue = queue.subList(0, 0);
					}
				}
				if (toFlush != null) { // Hold the lock for as little time as
					// possible
					model.addAllElements(toFlush);
					SwingUtilities.invokeLater(scrollToBottom);
				}
				try {
					Thread.sleep(LogQueue.FLUSH_RATE);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	private static class Renderer implements ListCellRenderer {

		private final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
		private final Border SELECTED_BORDER = UIManager
				.getBorder("List.focusCellHighlightBorder");
		private final Color DARK_GREEN = new Color(0, 90, 0);

		public Component getListCellRendererComponent(final JList list,
		                                              final Object value, final int index, final boolean isSelected,
		                                              final boolean cellHasFocus) {
			if (!(value instanceof WrappedLogRecord)) {
				return new JLabel();
			}
			final WrappedLogRecord wlr = (WrappedLogRecord) value;

			final JTextPane result = new JTextPane();
			result.setDragEnabled(true);
			result.setText(wlr.formatted);
			result.setComponentOrientation(list.getComponentOrientation());
			result.setFont(list.getFont());
			result.setBorder(cellHasFocus || isSelected ? SELECTED_BORDER
					: EMPTY_BORDER);

			result.setForeground(Color.DARK_GRAY);
			result.setBackground(Color.WHITE);

			if (wlr.record.getLevel() == Level.SEVERE) {
				result.setBackground(Color.RED);
				result.setForeground(Color.WHITE);
			}

			if (wlr.record.getLevel() == Level.WARNING) {
				result.setForeground(Color.RED);
			}

			if ((wlr.record.getLevel() == Level.FINE)
					|| (wlr.record.getLevel() == Level.FINER)
					|| (wlr.record.getLevel() == Level.FINEST)) {
				result.setForeground(DARK_GREEN);
			}

			Object[] parameters = wlr.record.getParameters();
			if (parameters != null) {
				for (Object parameter : parameters) {
					if (parameter == null) {
						continue;
					}

					if (parameter instanceof Color) {
						result.setForeground((Color) parameter);
					} else if (parameter instanceof Font) {
						result.setFont((Font) parameter);
					}
				}
			}

			return result;
		}

	}

	/**
	 * Wrap the log records so we can control the copy paste text (via
	 * #toString)
	 */
	private class WrappedLogRecord {

		public final LogRecord record;
		public final String formatted;

		public WrappedLogRecord(final LogRecord record) {
			this.record = record;
			formatted = LogTextArea.formatter.format(record);
		}

		@Override
		public String toString() {
			return LogTextArea.copyPasteFormatter.format(record);
		}

	}

}