package rsb.walker.dax_api.api_lib.models;



public class BankPathRequestPair {

    private Point3D start;
    private RunescapeBank bank;

    public BankPathRequestPair(Point3D start, RunescapeBank bank) {
        this.start = start;
        this.bank = bank;
    }

    public Point3D getStart() {
        return start;
    }

    public RunescapeBank getBank() {
        return bank;
    }
}
