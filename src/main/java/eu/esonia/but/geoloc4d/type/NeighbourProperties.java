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
        if (representation.length() != 1) {
            throw new NodeParsingException("Unset or multiple ID!");
        }
        this.setID((String) representation.keys().next());
        JSONObject properties = representation.getJSONObject(this.getID());
        if (properties.has("distance")) {
            this.setDistance(properties.getDouble("distance"));
        }
        if (properties.has("locationAbsolute")) {
            this.setLocationAbsolute(new Vector3D(properties.getJSONArray("locationAbsolute")));
        }
        if (properties.has("locationRelative")) {
            this.setLocationRelative(new Vector3D(properties.getJSONArray("locationRelative")));
        }
        if (properties.has("rssi")) {
            this.setRssi((short) properties.getInt("rssi"));
        }
        if (properties.has("rtt")) {
            this.setRtt(properties.getDouble("rtt"));
        }
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public NeighbourProperties(final NeighbourProperties source) {
        super(source);
        this.distance = ( source.getDistance() != null )
                ? new Double(source.getDistance()) : null;
        this.locationRelative = ( source.getLocationRelative() != null )
                ? new Vector3D(source.getLocationRelative()) : null;
        this.rssi = ( source.getRssi() != null )
                ? new Short(source.getRssi()) : null;
        this.rtt = ( source.getRtt() != null )
                ? new Double(source.getRtt()) : null;
    }

    @Override
    public JSONObject toJSONObject() {
        try {
            JSONObject resultProps = new JSONObject();
            resultProps.putOpt("distance", this.getDistance());
            if (this.getLocationAbsolute() != null) {
                resultProps.put("locationAbsolute", this.getLocationAbsolute().toJSONArray());
            }
            if (this.getLocationRelative() != null) {
                resultProps.put("locationRelative", this.getLocationRelative().toJSONArray());
            }
            resultProps.putOpt("rssi", this.getRssi());
            resultProps.putOpt("rtt", this.getRtt());
            return new JSONObject().put(this.getID(), resultProps);
            // e.g. "FirstNode":{"distance":1414.2,"rssi":175}
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
