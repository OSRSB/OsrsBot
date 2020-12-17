package rsb.walker.dax_api.api_lib.models;


public class DataLogRequest {


    private String user;


    private String source;


    private String propertyName;


    private double value;

    public DataLogRequest(String user, String source, String propertyName, double value) {
        this.user = user;
        this.source = source;
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getUser() {
        return user;
    }

    public String getSource() {
        return source;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public double getValue() {
        return value;
    }
}
