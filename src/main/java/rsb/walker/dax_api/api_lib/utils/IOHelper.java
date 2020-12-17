package rsb.walker.dax_api.api_lib.utils;

import com.google.gson.JsonObject;
import rsb.walker.dax_api.api_lib.models.DaxCredentials;
import rsb.walker.dax_api.api_lib.models.DaxCredentialsProvider;
import rsb.walker.dax_api.api_lib.models.ServerResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class IOHelper {

    public static String readInputStream(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return null;
        }
    }

    public static ServerResponse get(String endpoint, DaxCredentialsProvider daxCredentialsProvider) throws IOException {
        URL myurl = new URL(endpoint);
        HttpURLConnection connection = (HttpsURLConnection) myurl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestProperty("Method", "GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        if (daxCredentialsProvider != null) {
            appendAuth(connection, daxCredentialsProvider);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return new ServerResponse(false, connection.getResponseCode(), IOHelper.readInputStream(connection.getErrorStream()));
        }

        String contents = IOHelper.readInputStream(connection.getInputStream());
        return new ServerResponse(true, HttpURLConnection.HTTP_OK, contents);
    }

    public static ServerResponse post(JsonObject jsonObject, String endpoint, DaxCredentialsProvider daxCredentialsProvider) throws IOException {
        URL myurl = new URL(endpoint);
        HttpURLConnection connection = (HttpsURLConnection) myurl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestProperty("Method", "POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        if (daxCredentialsProvider != null) {
            appendAuth(connection, daxCredentialsProvider);
        }

        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return new ServerResponse(false, connection.getResponseCode(), IOHelper.readInputStream(connection.getErrorStream()));
        }

        String contents = IOHelper.readInputStream(connection.getInputStream());
        return new ServerResponse(true, HttpURLConnection.HTTP_OK, contents);
    }

    public static void appendAuth(HttpURLConnection connection, DaxCredentialsProvider daxCredentialsProvider) {
        if (daxCredentialsProvider != null && daxCredentialsProvider.getDaxCredentials() != null) {
            DaxCredentials daxCredentials = daxCredentialsProvider.getDaxCredentials();
            connection.setRequestProperty("key", daxCredentials.getApiKey());
            connection.setRequestProperty("secret", daxCredentials.getSecretKey());
        }
    }

}
