package rsb.walker.dax_api.api_lib.models;



import com.google.gson.Gson;

import java.util.List;


public class UserHighScore {


    private String propertyName;


    private Period period;


    private List<String> sources;

    public String getPropertyName() {
        return propertyName;
    }

    public Period getPeriod() {
        return period;
    }

    public List<String> getSources() {
        return sources;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
