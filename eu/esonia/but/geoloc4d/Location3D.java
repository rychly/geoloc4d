package eu.esonia.but.geoloc4d;

/**
 * Location coordinates in 3D.
 * @author rychly
 */
public class Location3D {

    private Integer x;
    private Integer y;
    private Integer z;

    Location3D() {
        this.x = null;
        this.y = null;
        this.z = null;
    }

    Location3D(int x, int y, int z) {
        this.set(x, y, z);
    }

    Location3D(Location3D location) {
        this.set(location);
    }

    Location3D(String location) {
        this.set(location);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public void set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(Location3D location) {
        this.set(location.x, location.y, location.z);
    }

    public void set(String location) {
        String[] tokens = location.split(" *[(;)] *", 5);
        this.setX(Integer.parseInt(tokens[1]));
        this.setY(Integer.parseInt(tokens[2]));
        this.setZ(Integer.parseInt(tokens[3]));
    }

    @Override
    public String toString() {
        return new String(Location3D.class.getSimpleName() + "(" + this.getX() + ";" + this.getY() + ";" + this.getZ() + ")");
    }
}
