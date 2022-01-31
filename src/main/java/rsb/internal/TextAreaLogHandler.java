package rsb.internal;

import rsb.plugin.LogTextArea;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TextAreaLogHandler extends Handler {

	public static final LogTextArea TEXT_AREA = new LogTextArea();

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(final LogRecord record) {
		TextAreaLogHandler.TEXT_AREA.log(record);
	}

}
