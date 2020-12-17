package rsb.walker.dax_api.api_lib.models;

public class DaxCredentials {

    private String apiKey, secretKey;

    public DaxCredentials(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
