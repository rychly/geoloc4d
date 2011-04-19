package eu.esonia.but.geoloc4d;

/**
 * Properties of a node as seen from its neighbouring node.
 * @author rychly
 */
public final class NodeProperties extends NodeData {

    /**
     * Distance of a node from an active node.
     */
    public Double distance;
    /**
     * Relative location of a node from an active node.
     */
    public Vector3D locationRelative;
    /**
     * RSSI network property of the node.
     * IEEE 802's RSSI are unitless and in the range 0 to 255, expressible as a one-byte unsigned integer.
     */
    public Short rssi;
    /**
     * Round-trip-time network property to the node.
     */
    public Double rtt;

    /**
     * Constructor of a node from its identificator and string representation.
     * @param id identificator of the node properties
     * @param representation string representation of the node properties
     * @exception NodePropertiesException fail to parse the string representation
     */
    NodeProperties(final String id, final String representation) throws NodePropertiesException {
        super(id, representation);
    }

    /**
     * Constructor of a node from its string representation.
     * @param representation string representation of the node properties
     * @exception NodePropertiesException fail to parse the string representation
     */
    NodeProperties(final String representation) throws NodePropertiesException {
        super(representation);
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    NodeProperties(final NodeProperties source) {
        super(source);
        this.distance = source.distance;
        this.locationRelative = source.locationRelative;
        this.rssi = source.rssi;
        this.rtt = source.rtt;
    }

    @Override
    public String toString() {
        String result = this.getID() + " { ";
        if (this.distance != null) {
            result = result.concat("distance=" + this.distance.toString() + "; ");
        }
        if (this.locationAbsolute != null) {
            result = result.concat("locationAbsolute=" + this.locationAbsolute.toString() + "; ");
        }
        if (this.locationRelative != null) {
            result = result.concat("locationRelative=" + this.locationRelative.toString() + "; ");
        }
        if (this.rssi != null) {
            result = result.concat("rssi=" + this.rssi.toString() + "; ");
        }
        if (this.rtt != null) {
            result = result.concat("rtt=" + this.rtt.toString() + "; ");
        }
        return result.concat("}");
    }

    /**
     * Set node properties from its string representation. It's reverese operation to toString method.
     * @param representation string representation of the node properties
     * @exception NodePropertiesException fail to parse the string representation
     */
    @Override
    public void set(final String representation) throws NodePropertiesException {
        String[] tokens = representation.split("\\s*([{=;}]\\s*)+");
        if (!tokens[0].isEmpty()) {
            this.setID(tokens[0]);
        }
        for (int i = 1; i < tokens.length; i += 2) {
            if (tokens[i].equalsIgnoreCase("distance")) {
                this.distance = Double.parseDouble(tokens[i + 1]);
            } else if (tokens[i].equalsIgnoreCase("locationAbsolute")) {
                this.locationAbsolute = new Vector3D(tokens[i + 1]);
            } else if (tokens[i].equalsIgnoreCase("locationRelative")) {
                this.locationRelative = new Vector3D(tokens[i + 1]);
            } else if (tokens[i].equalsIgnoreCase("rssi")) {
                this.rssi = Short.parseShort(tokens[i + 1]);
            } else if (tokens[i].equalsIgnoreCase("rtt")) {
                this.rtt = Double.parseDouble(tokens[i + 1]);
            } else {
                throw new NodePropertiesException("Unknown property " + tokens[i]);
            }
        }
    }
}
