package rsb.walker.dax_api.api_lib;

import rsb.methods.Web;
import rsb.util.Timer;
import rsb.walker.dax_api.api_lib.models.*;
import rsb.walker.dax_api.api_lib.utils.DaxTrackerProperty;
import rsb.walker.dax_api.walker_engine.Loggable;
import rsb.wrappers.RSPlayer;

import java.util.ArrayList;
import java.util.List;

public class DaxTracker implements Loggable {

    public static void setCredentials(DaxCredentialsProvider daxCredentialsProvider) {
        DaxTrackerServerApi.getInstance().setDaxCredentialsProvider(daxCredentialsProvider);
    }


    /**
     *
     * @param period
     * @return
     */
    public static ListSearch usersOnline(Period period) {
        return DaxTrackerServerApi.getInstance().usersOnline(null, period);
    }

    /**

    /**
     *
     * @param propertyName
     * @param period Only show results within time period.
     * @return High score of Tribot Usernames for a specific property.
     */
    public static UserHighScore getUsersHighScore(String propertyName, Period period) {
        return DaxTrackerServerApi.getInstance().topUsers(propertyName, period);
    }

    /**
     *
     * @param propertyName
     * @param period Filters by only stats within period.
     * @return Top users starting from highest to lowest stats for the property.
     */
    public static UserHighScore getUserHighscore(String propertyName, Period period) {
        return DaxTrackerServerApi.getInstance().topUsers(propertyName, period);
    }

    private List<DaxTrackerProperty> trackerProperties;
    private long lastUpdated;
    private boolean started, stopped;

    public DaxTracker() {
        trackerProperties = new ArrayList<>();
        lastUpdated = System.currentTimeMillis();
        started = false;
        stopped = false;
    }

    public void add(DaxTrackerProperty daxTrackerProperty) {
        trackerProperties.add(daxTrackerProperty);
    }

    public boolean update() {
        if (Timer.timeFromMark(lastUpdated) < 10000) {
            return false;
        }

        for (DaxTrackerProperty daxTrackerProperty : trackerProperties) {
            double value = daxTrackerProperty.differenceSinceLastTracked();
            if (daxTrackerProperty.update()) {
                log(daxTrackerProperty.getName(), value);
                log("Logged " + daxTrackerProperty.getName());
            } else {
                log("Refused update " + daxTrackerProperty.getName() + " [Exceeded maximum acceptable value]");
            }
        }

        lastUpdated = System.currentTimeMillis();
        return true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("DaxTracker already started!");
        }
        this.started = true;

        new Thread(() -> {
           while (!isStopped()) {
               update();
               Web.methods.web.sleep(15000);
           }
        }).start();

    }

    public void stop() {
        this.started = false;
        this.stopped = true;
    }


    @Override
    public String getName() {
        return "DaxTracker";
    }

    private static void log(String propertyName, double value) {
        RSPlayer player = Web.methods.players.getMyPlayer();
        String accountName = player != null ? player.getName().replaceAll("[^a-zA-Z0-9]", " ") : null;
        DaxTrackerServerApi.getInstance().log(
                "NULL",
                accountName,
                propertyName,
                value
        );
    }
}
