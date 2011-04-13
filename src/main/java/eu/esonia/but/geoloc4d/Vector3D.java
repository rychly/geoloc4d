package eu.esonia.but.geoloc4d;

/**
 * Vector coordinates in 3D.
 * @author rychly
 */
public class Vector3D {

    private boolean undefined;
    private double x;
    private double y;
    private double z;
    /**
     * Null vector.
     */
    public static final Vector3D NULL = new Vector3D(0, 0, 0);

    Vector3D() {
        this.undefined = true;
        this.x = this.y = this.z = 0;
    }

    Vector3D(double x, double y, double z) {
        this.set(x, y, z);
    }

    Vector3D(final Vector3D vector) {
        this.set(vector);
    }

    Vector3D(final String vector) {
        this.set(vector);
    }

    public boolean isUndefined() {
        return this.undefined;
    }

    public void setX(double x) {
        this.x = x;
        this.undefined = false;
    }

    public void setY(double y) {
        this.y = y;
        this.undefined = false;
    }

    public void setZ(double z) {
        this.z = z;
        this.undefined = false;
    }

    public Double getX() {
        return this.undefined ? null : this.x;
    }

    public Double getY() {
        return this.undefined ? null : this.y;
    }

    public Double getZ() {
        return this.undefined ? null : this.z;
    }

    /**
     * Set vector's coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.undefined = false;
    }

    /**
     * Copy coordinates of vector from another.
     * @param vector vector of original coordinates
     */
    public void set(final Vector3D vector) {
        this.set(vector.x, vector.y, vector.z);
    }

    /**
     * Set vector coordinates from string (e.g. "Vector3D(1.23,4.56,7.89)").
     * @param vector string with coordinates of vector
     */
    public void set(final String vector) {
        String[] tokens = vector.split(" *[(;)] *", 5);
        this.set(Double.parseDouble(tokens[1]),
                Double.parseDouble(tokens[2]),
                Double.parseDouble(tokens[3]));
    }

    @Override
    public String toString() {
        return Vector3D.class.getSimpleName()
                + "(" + this.getX().toString()
                + ";" + this.getY().toString()
                + ";" + this.getZ().toString()
                + ")";
    }

    public boolean equals(final Vector3D vector) {
        return (this.getX() == vector.getX())
                && (this.getY() == vector.getY())
                && (this.getZ() == vector.getZ());
    }

    /**
     * Addition of another vector.
     * @param increment increment
     * @return result
     */
    public Vector3D add(final Vector3D increment) {
        return new Vector3D(this.getX() + increment.getX(),
                this.getY() + increment.getY(),
                this.getZ() + increment.getZ());
    }

    /**
     * Substraction of another vector.
     * @param subtrahend decrement
     * @return result
     */
    public Vector3D sub(final Vector3D subtrahend) {
        return new Vector3D(this.getX() - subtrahend.getX(),
                this.getY() - subtrahend.getY(),
                this.getZ() - subtrahend.getZ());
    }

    /**
     * Distance of vector from target.
     * @param target target vector
     * @return distance
     */
    public double distance(final Vector3D target) {
        return Math.sqrt(Math.pow(this.getX() - target.getX(), 2)
                + Math.pow(this.getY() - target.getY(), 2)
                + Math.pow(this.getZ() - target.getZ(), 2));
    }

    /**
     * Norm of vector.
     * @return norm
     */
    public double norm() {
        return this.distance(Vector3D.NULL);
    }

    /**
     * Multiplication of vector by scalar value.
     * @param multiplier multiplier
     * @return result
     */
    public Vector3D scale(double multiplier) {
        return new Vector3D(multiplier * this.getX(),
                multiplier * this.getY(),
                multiplier * this.getZ());
    }

    /**
     * Dot operation on vector and another.
     * @param vector second vector
     * @return result
     */
    public double dot(final Vector3D vector) {
        return this.getX() * vector.getX()
                + this.getY() * vector.getY()
                + this.getZ() * vector.getZ();
    }

    /**
     * Cross operation of vector and another.
     * @param vector second vector
     * @return result
     */
    public Vector3D cross(final Vector3D vector) {
        return new Vector3D(this.getY() * vector.getZ() - this.getZ() * vector.getY(),
                this.getZ() * vector.getX() - this.getX() * vector.getZ(),
                this.getX() * vector.getY() - this.getY() * vector.getX());
    }

    /**
     * Average vector from this and another.
     * @param vector second vector
     * @return average
     */
    public Vector3D avg(final Vector3D vector) {
        return new Vector3D((this.getX() + vector.getX()) / 2,
                (this.getY() + vector.getY()) / 2,
                (this.getZ() + vector.getZ()) / 2);
    }

    /**
     * Select two most similar vectors from group of vectors (based on their distance).
     * @param vectors group of vectors
     * @return pair of the most similar vectors
     */
    public static Vector3D[] selectTwoMostSimilar(final Vector3D[] vectors) {
        Vector3D[] result = new Vector3D[2];
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < vectors.length; i++) {
            for (int j = i + 1; j < vectors.length; j++) {
                if (vectors[i].distance(vectors[j]) < minDistance) {
                    result[0] = vectors[i];
                    result[1] = vectors[j];
                }
            }
        }
        return result;
    }
}