package eu.esonia.but.geoloc4d.type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONString;

/**
 * Vector coordinates in 3D.
 *
 * @author rychly
 */
public final class Vector3D implements JSONString {

    private boolean undefined;
    private double x;
    private double y;
    private double z;
    /**
     * Null vector.
     */
    public static final Vector3D NULL = new Vector3D(0, 0, 0);

    /**
     * Create an undefined vector.
     */
    public Vector3D() {
        this.undefined = true;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Create a vector fro its coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vector3D(double x, double y, double z) {
        this.set(x, y, z);
    }

    /**
     * Create a copy of a given vector.
     *
     * @param vector vector of original coordinates
     */
    public Vector3D(final Vector3D vector) {
        if (( vector == null ) || vector.isUndefined()) {
            this.undefined = true;
        } else {
            this.set(vector.getX(), vector.getY(), vector.getZ());
        }
    }

    /**
     * Create a vector from coordinates JSON array string (e.g.
     * "[1.23,4.56,7.89]").
     *
     * @param vector string with coordinates of vector
     * @throws JSONException fail to parse the vector in JSON
     */
    public Vector3D(final String vector) throws JSONException {
        this(vector.isEmpty() ? null : new JSONArray(vector));
    }

    /**
     * Create a vector from coordinates in JSONArray.
     *
     * @param vector JSONArray with coordinates of vector
     * @throws JSONException fail to parse the vector in JSON
     */
    public Vector3D(final JSONArray vector) throws JSONException {
        if (( vector == null ) || ( vector.length() != 3 )) {
            this.undefined = true;
        } else {
            this.set(vector.getDouble(0),
                    vector.getDouble(1),
                    vector.getDouble(2));
        }
    }

    public boolean isDefined() {
        return !this.undefined;
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
     *
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
     * Create JSONArray from the vector.
     *
     * @return the JSONArray representing the vector
     */
    public JSONArray toJSONArray() {
        JSONArray result = new JSONArray();
        if (!this.isUndefined()) {
            result.put(this.getX());
            result.put(this.getY());
            result.put(this.getZ());
        }
        return result;
    }

    @Override
    public String toString() {
        return this.toJSONArray().toString();
    }

    @Override
    public String toJSONString() {
        return this.toJSONArray().toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (!( object instanceof Vector3D )) {
            return false;
        } else {
            Vector3D vector = (Vector3D) object;
            return !( this.isUndefined() || vector.isUndefined() )
                    && this.getX().equals(vector.getX())
                    && this.getY().equals(vector.getY())
                    && this.getZ().equals(vector.getZ());
        }
    }

    @Override
    public int hashCode() {
        if (this.isUndefined()) {
            return 0;
        } else {
            int hash = 7;
            hash = 53 * hash + (int) ( Double.doubleToLongBits(this.x) ^ ( Double.doubleToLongBits(this.x) >>> 32 ) );
            hash = 53 * hash + (int) ( Double.doubleToLongBits(this.y) ^ ( Double.doubleToLongBits(this.y) >>> 32 ) );
            hash = 53 * hash + (int) ( Double.doubleToLongBits(this.z) ^ ( Double.doubleToLongBits(this.z) >>> 32 ) );
            return hash;
        }
    }

    /**
     * Addition of another vector.
     *
     * @param increment increment
     * @return result
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public Vector3D add(final Vector3D increment) throws Vector3DUndefinedException {
        if (this.isUndefined() || increment.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return new Vector3D(this.getX() + increment.getX(),
                    this.getY() + increment.getY(),
                    this.getZ() + increment.getZ());
        }
    }

    /**
     * Substraction of another vector.
     *
     * @param subtrahend decrement
     * @return result
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public Vector3D sub(final Vector3D subtrahend) throws Vector3DUndefinedException {
        if (this.isUndefined() || subtrahend.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return new Vector3D(this.getX() - subtrahend.getX(),
                    this.getY() - subtrahend.getY(),
                    this.getZ() - subtrahend.getZ());

        }
    }

    /**
     * Distance of vector from target.
     *
     * @param target target vector
     * @return distance
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public double distance(final Vector3D target) throws Vector3DUndefinedException {
        if (this.isUndefined() || target.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return Math.sqrt(Math.pow(this.getX() - target.getX(), 2)
                    + Math.pow(this.getY() - target.getY(), 2)
                    + Math.pow(this.getZ() - target.getZ(), 2));
        }
    }

    /**
     * Norm of vector.
     *
     * @return norm
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public double norm() throws Vector3DUndefinedException {
        if (this.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return this.distance(Vector3D.NULL);
        }
    }

    /**
     * Multiplication of vector by scalar value.
     *
     * @param multiplier multiplier
     * @return result
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public Vector3D scale(final double multiplier) throws Vector3DUndefinedException {
        if (this.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return new Vector3D(multiplier * this.getX(),
                    multiplier * this.getY(),
                    multiplier * this.getZ());
        }
    }

    /**
     * Dot operation on vector and another.
     *
     * @param vector second vector
     * @return result
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public double dot(final Vector3D vector) throws Vector3DUndefinedException {
        if (this.isUndefined() || vector.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return this.getX() * vector.getX()
                    + this.getY() * vector.getY()
                    + this.getZ() * vector.getZ();
        }
    }

    /**
     * Cross operation of vector and another.
     *
     * @param vector second vector
     * @return result
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public Vector3D cross(final Vector3D vector) throws Vector3DUndefinedException {
        if (this.isUndefined() || vector.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return new Vector3D(this.getY() * vector.getZ() - this.getZ() * vector.getY(),
                    this.getZ() * vector.getX() - this.getX() * vector.getZ(),
                    this.getX() * vector.getY() - this.getY() * vector.getX());
        }
    }

    /**
     * Average vector from this and another.
     *
     * @param vector second vector
     * @return average
     * @throws Vector3DUndefinedException operation with undefined vector
     */
    public Vector3D avg(final Vector3D vector) throws Vector3DUndefinedException {
        if (this.isUndefined() || vector.isUndefined()) {
            throw new Vector3DUndefinedException();
        } else {
            return new Vector3D(( this.getX() + vector.getX() ) / 2,
                    ( this.getY() + vector.getY() ) / 2,
                    ( this.getZ() + vector.getZ() ) / 2);
        }
    }

    /**
     * Select two most similar vectors from group of vectors (based on their
     * distance).
     *
     * @param vectors group of vectors
     * @return pair of the most similar vectors
     */
    public static Vector3D[] selectTwoMostSimilar(final Vector3D[] vectors) {
        Vector3D[] result = new Vector3D[2];
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < vectors.length; i++) {
            for (int j = i + 1; j < vectors.length; j++) {
                if (!( ( vectors[i] == null ) || vectors[i].isUndefined()
                        || ( vectors[j] == null ) || vectors[j].isUndefined() )) {
                    double distance = vectors[i].distance(vectors[j]);
                    if (distance < minDistance) {
                        result[0] = vectors[i];
                        result[1] = vectors[j];
                        minDistance = distance;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Select two most different vectors from group of vectors (based on their
     * distance).
     *
     * @param vectors group of vectors
     * @return pair of the most different vectors
     */
    public static Vector3D[] selectTwoMostDifferent(final Vector3D[] vectors) {
        Vector3D[] result = new Vector3D[2];
        double maxDistance = Double.MIN_VALUE;
        for (int i = 0; i < vectors.length; i++) {
            for (int j = i + 1; j < vectors.length; j++) {
                if (!( ( vectors[i] == null ) || vectors[i].isUndefined()
                        || ( vectors[j] == null ) || vectors[j].isUndefined() )) {
                    double distance = vectors[i].distance(vectors[j]);
                    if (distance > maxDistance) {
                        result[0] = vectors[i];
                        result[1] = vectors[j];
                        maxDistance = distance;
                    }
                }
            }
        }
        return result;
    }
}
