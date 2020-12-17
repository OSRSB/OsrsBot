package rsb.walker.dax_api.walker.utils.camera;


public class Calculations {

    public static int log(double x, double base) {
        return (int) (Math.log(x) / Math.log(base));
    }

    public static int limitRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

}
