package rsb.walker.dax_api.api_lib.utils;

public abstract class DaxTrackerProperty {

    private String name;
    private double lastTrackedValue;

    public DaxTrackerProperty(String name) {
        this.name = name;
        this.lastTrackedValue = currentValue();
    }

    public abstract double currentValue();

    /**
     * Max value tracked per log otherwise ignored. This is to prevent unexpected data.
     *
     * Such as EXP gain on login might be tracked
     *
     * @return
     */
    public abstract double maxAcceptableChange();

    public double differenceSinceLastTracked() {
        return currentValue() - lastTrackedValue;
    }

    public boolean update() {
        if (differenceSinceLastTracked() > Math.abs(maxAcceptableChange())) {
            return false;
        }

        this.lastTrackedValue = currentValue();
        return true;
    }

    public String getName() {
        return name;
    }
}
