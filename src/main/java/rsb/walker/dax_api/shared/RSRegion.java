package rsb.walker.dax_api.shared;


public class RSRegion {

    private int baseX, baseY, baseZ;

    public RSRegion(int baseX, int baseY, int baseZ){
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
    }

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public int getBaseZ() {
        return baseZ;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (!(obj instanceof RSRegion)){
            return false;
        }
        RSRegion compare = (RSRegion) obj;
        return compare.baseX == baseX && compare.baseY == baseY && compare.baseZ == baseZ;
    }

    @Override
    public int hashCode(){
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(baseX);
        bits = 31L * bits + Double.doubleToLongBits(baseY);
        bits = 31L * bits + Double.doubleToLongBits(baseZ);
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public String toString(){
        return "Region[" + baseX + ", " + baseY + ", " + baseZ + "]";
    }

    static class Rectangle{
        int x, y, width, height;
        Rectangle(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this. height = height;
        }
        Rectangle(int x, int y){
            this.x = x;
            this.y = y;
        }

        void add(int newx, int newy){
            if ((width | height) < 0) {
                this.x = newx;
                this.y = newy;
                this.width = this.height = 0;
                return;
            }
            int x1 = this.x;
            int y1 = this.y;
            long x2 = this.width;
            long y2 = this.height;
            x2 += x1;
            y2 += y1;
            if (x1 > newx) x1 = newx;
            if (y1 > newy) y1 = newy;
            if (x2 < newx) x2 = newx;
            if (y2 < newy) y2 = newy;
            x2 -= x1;
            y2 -= y1;
            if (x2 > Integer.MAX_VALUE) x2 = Integer.MAX_VALUE;
            if (y2 > Integer.MAX_VALUE) y2 = Integer.MAX_VALUE;
            reshape(x1, y1, (int) x2, (int) y2);
        }

        void reshape(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
