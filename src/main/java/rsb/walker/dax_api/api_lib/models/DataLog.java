package rsb.walker.dax_api.api_lib.models;


import com.google.gson.Gson;


public class DataLog {


    private String id;


    private String timeStamp;


    private String group;


    private String user;


    private String source;


    private String propertyName;


    private double value;

    public DataLog(String id, String timeStamp, String group, String user, String source, String propertyName, double value) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.group = group;
        this.user = user;
        this.source = source;
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getGroup() {
        return group;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
