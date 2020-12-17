package rsb.walker.dax_api.api_lib.models;





public class DataSnapshot {


    private String timeStamp;


    private double total;

    public DataSnapshot() {
    }

    public DataSnapshot(String timeStamp, double total) {
        this.timeStamp = timeStamp;
        this.total = total;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getTotal() {
        return total;
    }
}
