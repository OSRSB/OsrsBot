package rsb.walker.dax_api.walker.models.enums;

public enum ActionResult {

    CONTINUE (true), //Continues walker

    FAILURE (false), // Continues walker; indicates fail

    EXIT_SUCCESS (true), // Exits walker; indicates success

    EXIT_FAILURE (false) // Exits walker; indicates fail

    ;

    private boolean success;

    ActionResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
