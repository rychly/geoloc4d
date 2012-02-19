package eu.esonia.but.geoloc4d.type;

import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Properties of a node as seen from its neighbouring node.
 *
 * @author rychly
 */
public final class NeighbourProperties extends NodeData {

    /**
     * Distance of a node from an active node.
     */
    private Double distance;
    /**
     * Relative location of a node from an active node.
     */
    private Vector3D locationRelative;
    /**
     * RSSI network property of the node. IEEE 802's RSSI are unitless and in
     * the range 0 to 255, expressible as a one-byte unsigned integer.
     */
    private Short rssi;
    /**
     * Round-trip-time network property to the node.
     */
    private Double rtt;

    /**
     * Empty constructor for an initiation from subclasses.
     */
    protected NeighbourProperties() {
    }

    /**
     * Constructor of a node from its string representation in JSON.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the string representation in JSON
     */
    public NeighbourProperties(final String representation) throws NodeParsingException, JSONException {
        this(new JSONObject(representation));
    }

    /**
     * Constructor of a node from its representation as JSONObject.
     *
     * @param representation representation of the node properties in JSONObject
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the representation in JSON
     */
    public NeighbourProperties(final JSONObject representation) throws NodeParsingException, JSONException {
        // id
        if (!representation.has("id")) {
            throw new NodeParsingException("Unset 'id'!");
        }
        this.setID(representation.getString("id"));
        // ip
        if (representation.has("ip")) {
            this.setIP(representation.getString("ip"));
        }
        // distance
        if (representation.has("distance")) {
            this.setDistance(representation.getDouble("distance"));
        }
        // locationAbsolute
        if (representation.has("locationAbsolute")) {
            this.setLocationAbsolute(new Vector3D(representation.getJSONArray("locationAbsolute")));
        }
        // locationRelative
        if (representation.has("locationRelative")) {
            this.setLocationRelative(new Vector3D(representation.getJSONArray("locationRelative")));
        }
        // rssi
        if (representation.has("rssi")) {
            this.setRssi((short) representation.getInt("rssi"));
        }
        // rtt
        if (representation.has("rtt")) {
            this.setRtt(representation.getDouble("rtt"));
        }
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public NeighbourProperties(final NeighbourProperties source) {
        super(source);
        Double doubleNumber = source.getDistance();
        if (doubleNumber != null) {
            this.setDistance(new Double(doubleNumber));
        }
        Vector3D vector = source.getLocationRelative();
        if (vector != null) {
            this.setLocationRelative(new Vector3D(vector));
        }
        Short shortNumber = source.getRssi();
        if (shortNumber != null) {
            this.setRssi(new Short(shortNumber));
        }
        doubleNumber = source.getRtt();
        if (doubleNumber != null) {
            this.setRtt(new Double(doubleNumber));
        }
    }

    @Override
    public JSONObject toJSONObject() {
        try {
            JSONObject result = new JSONObject();
            result.put("id", this.getID());
            result.putOpt("ip", this.getIP());
            result.putOpt("distance", this.getDistance());
            if (this.getLocationAbsolute() != null) {
                result.put("locationAbsolute", this.getLocationAbsolute().toJSONArray());
            }
            if (this.getLocationRelative() != null) {
                result.put("locationRelative", this.getLocationRelative().toJSONArray());
            }
            result.putOpt("rssi", this.getRssi());
            result.putOpt("rtt", this.getRtt());
            return result;
            // e.g. "{id:"SecondNode","locationAbsolute":[0,1000,0]}
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (!( object instanceof NeighbourProperties )) {
            return false;
        } else {
            NeighbourProperties neighbourProperties = (NeighbourProperties) object;
            return ( ( this.getID() == null && neighbourProperties.getID() == null ) || this.getID().equals(neighbourProperties.getID()) )
                    && ( ( this.getIP() == null && neighbourProperties.getIP() == null ) || this.getIP().equals(neighbourProperties.getIP()) )
                    && ( ( this.getDistance() == null && neighbourProperties.getDistance() == null ) || this.getDistance().equals(neighbourProperties.getDistance()) )
                    && ( ( this.getLocationAbsolute() == null && neighbourProperties.getLocationAbsolute() == null ) || this.getLocationAbsolute().equals(neighbourProperties.getLocationAbsolute()) )
                    && ( ( this.getLocationRelative() == null && neighbourProperties.getLocationRelative() == null ) || this.getLocationRelative().equals(neighbourProperties.getLocationRelative()) )
                    && ( ( this.getRssi() == null && neighbourProperties.getRssi() == null ) || this.getRssi().equals(neighbourProperties.getRssi()) )
                    && ( ( this.getRtt() == null && neighbourProperties.getRtt() == null ) || this.getRtt().equals(neighbourProperties.getRtt()) );
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getID());
        hash = 97 * hash + Objects.hashCode(this.getIP());
        hash = 97 * hash + Objects.hashCode(this.getDistance());
        hash = 97 * hash + Objects.hashCode(this.getLocationAbsolute());
        hash = 97 * hash + Objects.hashCode(this.getLocationRelative());
        hash = 97 * hash + Objects.hashCode(this.getRssi());
        hash = 97 * hash + Objects.hashCode(this.getRtt());
        return hash;
    }

    /**
     * @return the distance
     */
    public final Double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public final void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * @return the locationRelative
     */
    public final Vector3D getLocationRelative() {
        return locationRelative;
    }

    /**
     * @param locationRelative the locationRelative to set
     */
    public final void setLocationRelative(Vector3D locationRelative) {
        this.locationRelative = locationRelative;
    }

    /**
     * @return the rssi
     */
    public final Short getRssi() {
        return rssi;
    }

    /**
     * @param rssi the rssi to set
     */
    public final void setRssi(Short rssi) {
        this.rssi = rssi;
    }

    /**
     * @return the rtt
     */
    public final Double getRtt() {
        return rtt;
    }

    /**
     * @param rtt the rtt to set
     */
    public final void setRtt(Double rtt) {
        this.rtt = rtt;
    }
}
