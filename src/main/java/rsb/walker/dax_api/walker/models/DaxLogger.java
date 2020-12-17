package rsb.walker.dax_api.walker.models;

public interface DaxLogger {

    default void log(String message) {
        System.out.println(getPrefix() + message);
    }

    default void debug(String message) {
        System.out.println(getPrefix() + message); // ToDo: Add logic for debug logger.
    }

    default void error(String message) {
        System.out.println(getPrefix() + message); // ToDo: Add logic for error logger. Save to file.
    }

    default String getDebugName() {
        return this.getClass().getSimpleName();
    }

    default String getPrefix() {
        return String.format("[%s] ", getDebugName());
    }

}
