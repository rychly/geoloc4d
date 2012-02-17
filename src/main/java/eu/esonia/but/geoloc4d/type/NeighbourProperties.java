package eu.esonia.but.geoloc4d.type;

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
     * Constructor of a node from its identificator and string representation.
     *
     * @param id identificator of the node properties
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    public NeighbourProperties(final String id, final String representation) throws NodeParsingException {
        super(id, representation);
    }

    /**
     * Constructor of a node from its string representation.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    public NeighbourProperties(final String representation) throws NodeParsingException {
        super(representation);
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
    public String toString() {
        String result = this.getID() + " { ";
        if (this.getDistance() != null) {
            result = result.concat("distance=" + this.getDistance().toString() + "; ");
        }
        if (this.getLocationAbsolute() != null) {
            result = result.concat("locationAbsolute=" + this.getLocationAbsolute().toString() + "; ");
        }
        if (this.getLocationRelative() != null) {
            result = result.concat("locationRelative=" + this.getLocationRelative().toString() + "; ");
        }
        if (this.getRssi() != null) {
            result = result.concat("rssi=" + this.getRssi().toString() + "; ");
        }
        if (this.getRtt() != null) {
            result = result.concat("rtt=" + this.getRtt().toString() + "; ");
        }
        return result.concat("}");
    }

    @Override
    public String toJSONString() {
        try {
            JSONObject resultProps = new JSONObject();
            resultProps.putOpt("distance", this.getDistance());
            resultProps.putOpt("locationAbsolute", this.getLocationAbsolute());
            resultProps.putOpt("locationRelative", this.getLocationRelative());
            resultProps.putOpt("rssi", this.getRssi());
            resultProps.putOpt("rtt", this.getRtt());
            return new JSONObject().put(this.getID(), resultProps).toString();
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }

    /**
     * Set node properties from its string representation. It's reverese
     * operation to toString method.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    @Override
    public void set(final String representation) throws NodeParsingException {
        String[] tokens = representation.split("\\s*([{=;}]\\s*)+");
        if (!tokens[0].isEmpty()) {
            this.setID(tokens[0]);
        }
        for (int i = 1; i < tokens.length; i += 2) {
            if (tokens[i].equalsIgnoreCase("distance")) {
                this.setDistance((Double) Double.parseDouble(tokens[i + 1]));
            } else if (tokens[i].equalsIgnoreCase("locationAbsolute")) {
                this.setLocationAbsolute(new Vector3D(tokens[i + 1]));
            } else if (tokens[i].equalsIgnoreCase("locationRelative")) {
                this.setLocationRelative(new Vector3D(tokens[i + 1]));
            } else if (tokens[i].equalsIgnoreCase("rssi")) {
                this.setRssi((Short) Short.parseShort(tokens[i + 1]));
            } else if (tokens[i].equalsIgnoreCase("rtt")) {
                this.setRtt((Double) Double.parseDouble(tokens[i + 1]));
            } else {
                throw new NodeParsingException("Unknown property " + tokens[i]);
            }
        }
    }

    /**
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * @return the locationRelative
     */
    public Vector3D getLocationRelative() {
        return locationRelative;
    }

    /**
     * @param locationRelative the locationRelative to set
     */
    public void setLocationRelative(Vector3D locationRelative) {
        this.locationRelative = locationRelative;
    }

    /**
     * @return the rssi
     */
    public Short getRssi() {
        return rssi;
    }

    /**
     * @param rssi the rssi to set
     */
    public void setRssi(Short rssi) {
        this.rssi = rssi;
    }

    /**
     * @return the rtt
     */
    public Double getRtt() {
        return rtt;
    }

    /**
     * @param rtt the rtt to set
     */
    public void setRtt(Double rtt) {
        this.rtt = rtt;
    }
}
