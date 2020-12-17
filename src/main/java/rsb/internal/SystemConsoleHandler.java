package rsb.internal;

import java.util.logging.ConsoleHandler;

/**
 * Logs to System.out
 */
public class SystemConsoleHandler extends ConsoleHandler {
	public SystemConsoleHandler() {
		super();
		setOutputStream(System.out);
	}
}
