package rsb.walker.dax_api.walker.utils.generator;


public class CollisionTile {

    private int x, y, z;
    private boolean n, e, s, w, blocked, specialTile, initialized;

    public CollisionTile(int x, int y, int z, boolean n, boolean e, boolean s, boolean w, boolean blocked, boolean specialTile, boolean initialized){
        this.x = x;
        this.y = y;
        this.z = z;
        this.n = n;
        this.e = e;
        this.s = s;
        this.w = w;
        this.blocked = blocked;
        this.specialTile = specialTile;
        this.initialized = initialized;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isN() {
        return n;
    }

    public void setN(boolean n) {
        this.n = n;
    }

    public boolean isE() {
        return e;
    }

    public void setE(boolean e) {
        this.e = e;
    }

    public boolean isS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public boolean isW() {
        return w;
    }

    public void setW(boolean w) {
        this.w = w;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSpecialTile() {
        return specialTile;
    }

    public void setSpecialTile(boolean specialTile) {
        this.specialTile = specialTile;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public String toString(){
        return x + " "
                + y + " "
                + z + " "
                + convertToString(n) + " "
                + convertToString(e) + " "
                + convertToString(s) + " "
                + convertToString(w) + " "
                + convertToString(blocked) + " "
                + convertToString(specialTile) + " "
                + convertToString(initialized);
    }

    private String convertToString(boolean b) {
        return b ? "t" : "f";
    }
}
