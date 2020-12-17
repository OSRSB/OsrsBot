package rsb.walker.dax_api.api_lib.models;



import com.google.gson.Gson;

import java.util.List;


public class PropertyStats {


    private Period period;


    private String propertyName;


    private double total;


    private List<DataSnapshot> dataSnapshots;

    public PropertyStats() {
    }

    public PropertyStats(Period period, String propertyName, double total, List<DataSnapshot> dataSnapshots) {
        this.period = period;
        this.propertyName = propertyName;
        this.total = total;
        this.dataSnapshots = dataSnapshots;
    }

    public Period getPeriod() {
        return period;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public double getTotal() {
        return total;
    }

    public List<DataSnapshot> getDataSnapshots() {
        return dataSnapshots;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
