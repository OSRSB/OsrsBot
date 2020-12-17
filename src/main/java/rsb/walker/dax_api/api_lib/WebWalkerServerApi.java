package rsb.walker.dax_api.api_lib;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import rsb.walker.dax_api.api_lib.json.Json;
import rsb.walker.dax_api.api_lib.json.JsonValue;
import rsb.walker.dax_api.api_lib.json.ParseException;
import rsb.walker.dax_api.api_lib.models.*;
import rsb.walker.dax_api.api_lib.utils.IOHelper;
import rsb.walker.dax_api.walker_engine.Loggable;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WebWalkerServerApi implements Loggable {

    private static WebWalkerServerApi webWalkerServerApi;
    private static Gson gson = new Gson();

    public static WebWalkerServerApi getInstance() {
        return webWalkerServerApi != null ? webWalkerServerApi : (webWalkerServerApi = new WebWalkerServerApi());
    }

    private static final String WALKER_ENDPOINT = "https://api.dax.cloud", TEST_ENDPOINT = "http://localhost:8080";

    private static final String
            GENERATE_PATH = "/walker/generatePath",
            GENERATE_BANK_PATH = "/walker/generateBankPath";


    private DaxCredentialsProvider daxCredentialsProvider;
    private HashMap<String, String> cache;
    private boolean isTestMode;

    private WebWalkerServerApi() {
        cache = new HashMap<>();
    }

    public void setDaxCredentialsProvider(DaxCredentialsProvider daxCredentialsProvider) {
        this.daxCredentialsProvider = daxCredentialsProvider;
    }

    public List<PathResult> getPaths(BulkPathRequest bulkPathRequest) {
        try {
            return parseResults(post(gson.toJson(bulkPathRequest),WALKER_ENDPOINT + "/walker/generatePaths"));
        } catch(IOException e){
            getInstance().log("Is server down? Spam dax.");
            return Collections.singletonList(new PathResult(PathStatus.NO_RESPONSE_FROM_SERVER));
        }
    }

    public List<PathResult> getBankPaths(BulkBankPathRequest bulkBankPathRequest) {
        try {
            return parseResults(post(gson.toJson(bulkBankPathRequest),WALKER_ENDPOINT + "/walker/generateBankPaths"));
        } catch(IOException e){
            getInstance().log("Is server down? Spam dax.");
            return Collections.singletonList(new PathResult(PathStatus.NO_RESPONSE_FROM_SERVER));
        }
    }

    public PathResult getPath(Point3D start, Point3D end, PlayerDetails playerDetails) {
        com.google.gson.JsonObject pathRequest = new com.google.gson.JsonObject();
        pathRequest.add("start", start.toJson());
        pathRequest.add("end", end.toJson());

        if (playerDetails != null) {
            pathRequest.add("player", playerDetails.toJson());
        }

        try {
            return parseResult(post(pathRequest, (isTestMode ? TEST_ENDPOINT : WALKER_ENDPOINT) + GENERATE_PATH));
        } catch (IOException e) {
            getInstance().log("Is server down? Spam dax.");
            return new PathResult(PathStatus.NO_RESPONSE_FROM_SERVER);
        }

    }

    public PathResult getBankPath(Point3D start, RunescapeBank bank, PlayerDetails playerDetails) {
        com.google.gson.JsonObject pathRequest = new com.google.gson.JsonObject();

        pathRequest.add("start", start.toJson());

        if (bank != null) {
            pathRequest.addProperty("bank", bank.toString());
        }

        if (playerDetails != null) {
            pathRequest.add("player", playerDetails.toJson());
        }

        try {
            return parseResult(post(pathRequest, (isTestMode ? TEST_ENDPOINT : WALKER_ENDPOINT) + GENERATE_BANK_PATH));
        } catch (IOException e) {
            getInstance().log("Is server down? Spam dax.");
            return new PathResult(PathStatus.NO_RESPONSE_FROM_SERVER);
        }
    }

    public boolean isTestMode() {
        return isTestMode;
    }

    public void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }

    private List<PathResult> parseResults(ServerResponse serverResponse){
        if (!serverResponse.isSuccess()) {
            JsonValue jsonValue  = null;
            try{
                jsonValue = Json.parse(serverResponse.getContents());
            } catch(Exception | Error e){
                jsonValue = Json.NULL;
            }
            if (!jsonValue.isNull()) {
                getInstance().log("[Error] " + jsonValue.asObject().getString(
                        "message",
                        "Could not generate path: " + serverResponse.getContents()
                                                                             ));
            }

            switch (serverResponse.getCode()) {
                case 429:
                    return Collections.singletonList(new PathResult(PathStatus.RATE_LIMIT_EXCEEDED));
                case 400:
                case 401:
                case 404:
                    return Collections.singletonList(new PathResult(PathStatus.INVALID_CREDENTIALS));
            }
        }

        try {
            return gson.fromJson(serverResponse.getContents(), new TypeToken<List<PathResult>>() {}.getType());
        } catch (ParseException e) {
            PathResult pathResult = new PathResult(PathStatus.UNKNOWN);
            log("Error: " + pathResult.getPathStatus());
            return Collections.singletonList(pathResult);
        }
    }

    private PathResult parseResult(ServerResponse serverResponse) {
        if (!serverResponse.isSuccess()) {
            JsonValue jsonValue  = null;
            try{
                jsonValue = Json.parse(serverResponse.getContents());
            } catch(Exception | Error e){
                jsonValue = Json.NULL;
            }
            if (!jsonValue.isNull()) {
                getInstance().log("[Error] " + jsonValue.asObject().getString(
                        "message",
                        "Could not generate path: " + serverResponse.getContents()
                ));
            }

            switch (serverResponse.getCode()) {
                case 429:
                    return new PathResult(PathStatus.RATE_LIMIT_EXCEEDED);
                case 400:
                case 401:
                case 404:
                    return new PathResult(PathStatus.INVALID_CREDENTIALS);
            }
        }

        PathResult pathResult;
        JsonElement jsonObject;
        try {
            jsonObject = new JsonParser().parse(serverResponse.getContents());
        } catch (ParseException e) {
            pathResult = new PathResult(PathStatus.UNKNOWN);
            log("Error: " + pathResult.getPathStatus());
            return pathResult;
        }

        pathResult = PathResult.fromJson(jsonObject);
        log("Response: " + pathResult.getPathStatus() + " Cost: " + pathResult.getCost());
        return pathResult;
    }

    private ServerResponse post(com.google.gson.JsonObject jsonObject, String endpoint) throws IOException {
        return post(gson.toJson(jsonObject),endpoint);
    }

    private ServerResponse post(String json, String endpoint) throws IOException {
        getInstance().log("Generating path: " + json);
        if (cache.containsKey(json)) {
            return new ServerResponse(true, HttpURLConnection.HTTP_OK, cache.get(json.toString()));
        }

        URL myurl = new URL(endpoint);
        HttpURLConnection connection = (isTestMode ? (HttpURLConnection) myurl.openConnection() : (HttpsURLConnection) myurl.openConnection());
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestProperty("Method", "POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        IOHelper.appendAuth(connection, daxCredentialsProvider);

        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return new ServerResponse(false, connection.getResponseCode(), IOHelper.readInputStream(connection.getErrorStream()));
        }

        String contents = IOHelper.readInputStream(connection.getInputStream());
        cache.put(json, contents);
        return new ServerResponse(true, HttpURLConnection.HTTP_OK, contents);
    }


    @Override
    public String getName() {
        return "DaxWalker";
    }


}
