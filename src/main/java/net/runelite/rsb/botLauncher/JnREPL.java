package net.runelite.rsb.botLauncher;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JnREPL {

    final static Logger logger = LoggerFactory.getLogger(JnREPL.class);

    private static Object serverInstance = null;

    /**
     * Start a clojure nREPL server
     */
    public static synchronized void startRepl() {
        Long port = Long.parseLong(System.getProperty("jnrepl.port", "9090"));
        startRepl(port);
    }

    /**
     * Start a clojure nREPL server on the given port;
     */
    public static synchronized void startRepl(long port) {

        if (null != serverInstance) {
            logger.warn("nrepl server already running, refusing to start another.");
            return;
        }

        try {
            IFn require = Clojure.var("clojure.core", "require");
            require.invoke(Clojure.read("clojure.tools.nrepl.server"));
            IFn server = Clojure.var("clojure.tools.nrepl.server", "start-server");
            serverInstance = server.invoke(Clojure.read(":port"), port);

            logger.info("Started clojure nREPL on port {}", port);
        }
        catch (Throwable e) {
            logger.error("Error starting nrepl", e);
        }
    }

    /**
     * Shutdown the nREPL server
     */
    public static synchronized void shutdownRepl() {
        logger.info("Shutting down nrepl");
        if (null == serverInstance) {
            return;
        }

        IFn shutdownFn = Clojure.var("clojure.tools.nrepl.server", "stop-server");
        shutdownFn.invoke(serverInstance);
        serverInstance = null;
    }
}