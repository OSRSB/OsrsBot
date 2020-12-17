package rsb.walker.dax_api.api_lib;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import rsb.walker.dax_api.api_lib.models.*;
import rsb.walker.dax_api.api_lib.utils.IOHelper;
import rsb.walker.dax_api.walker_engine.Loggable;


import java.io.IOException;
import java.net.HttpURLConnection;

public class DaxTrackerServerApi implements Loggable {

    private static DaxTrackerServerApi webTrackerServerApi;

    public static DaxTrackerServerApi getInstance() {
        return webTrackerServerApi != null ? webTrackerServerApi : (webTrackerServerApi = new DaxTrackerServerApi());
    }

    private static final String TRACKER_ENDPOINT = "https://api.dax.cloud";


    private DaxCredentialsProvider daxCredentialsProvider;

    private DaxTrackerServerApi() {

    }

    public void setDaxCredentialsProvider(DaxCredentialsProvider daxCredentialsProvider) {
        this.daxCredentialsProvider = daxCredentialsProvider;
    }

    public ListSearch sourcesOnline(String propertyName, String user, Period period) {
        ServerResponse serverResponse;
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        try {
            serverResponse = IOHelper.get(
                    TRACKER_ENDPOINT + "/tracker/sources/online?propertyName=" + escaper.escape(propertyName)
                            + "&user=" + escaper.escape(user)
                            + (period != null ? "&period=" + period : ""),
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), ListSearch.class);
    }

    public ListSearch usersOnline(String propertyName, Period period) {
        ServerResponse serverResponse;
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        try {
            serverResponse = IOHelper.get(
                    TRACKER_ENDPOINT + "/tracker/users/online?"
                            + (propertyName != null ? "&propertyName=" + escaper.escape(propertyName) : "")
                            + (period != null ? "&period=" + period : ""),
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), ListSearch.class);
    }

    public SourceHighScore topSources(String user, String propertyName, Period period) {
        ServerResponse serverResponse;
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        try {
            serverResponse = IOHelper.get(
                    TRACKER_ENDPOINT + "/tracker/sources/top?propertyName=" + escaper.escape(propertyName)
                            + "&user=" + user
                            + (period != null ? "&period=" + period : ""),
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), SourceHighScore.class);
    }

    public UserHighScore topUsers(String propertyName, Period period) {
        ServerResponse serverResponse;
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        try {
            serverResponse = IOHelper.get(
                    TRACKER_ENDPOINT + "/tracker/users/top?propertyName=" + escaper.escape(propertyName)
                            + (period != null ? "&period=" + period : ""),
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), UserHighScore.class);
    }

    public PropertyStats getStats(String user, String source, String propertyName) {
        ServerResponse serverResponse;
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        try {
            serverResponse = IOHelper.get(
                    TRACKER_ENDPOINT + "/tracker/data?user=" + escaper.escape(user)
                            + "&propertyName=" + escaper.escape(propertyName)
                            + (source != null ? "&source=" + escaper.escape(source) : ""),
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), PropertyStats.class);
    }

    public DataLog log(String user, String source, String propertyName, double value) {
        ServerResponse serverResponse;
        try {
            serverResponse = IOHelper.post(
                    new JsonParser().parse(new Gson().toJson(new DataLogRequest(user, source, propertyName, value))).getAsJsonObject(),
                    TRACKER_ENDPOINT + "/tracker/data",
                    daxCredentialsProvider
            );
        } catch (IOException e) {
            return null;
        }

        if (serverResponse.getCode() != HttpURLConnection.HTTP_OK) {
            log("ERROR: " + new JsonParser().parse(serverResponse.getContents()).getAsJsonObject().get("message").getAsString());
            return null;
        }

        return new Gson().fromJson(serverResponse.getContents(), DataLog.class);
    }


    @Override
    public String getName() {
        return "daxTracker";
    }

}
