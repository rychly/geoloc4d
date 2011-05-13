package eu.esonia.but.geoloc4d.type;

/**
 * Basic data of a node.
 * @author rychly
 */
public class NodeData {

    /**
     * Identificator of the node (e.g. its name).
     */
    private String id;
    /**
     * Absolute location of the node.
     */
    private Vector3D locationAbsolute;

    /**
     * Constructor of a node from its identificator and string representation.
     * @param id identificator of the node properties
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    public NodeData(final String id, final String representation) throws NodeParsingException {
        this.set(representation);
        this.id = id;
    }

    /**
     * Constructor of a node from its string representation.
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    public NodeData(final String representation) throws NodeParsingException {
        this.set(representation);
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    public NodeData(final NodeData source) {
        this.id = source.getID();
        this.locationAbsolute = (source.getLocationAbsolute() != null)
                ? new Vector3D(source.getLocationAbsolute()) : null;
    }

    /**
     * Set identificator of the node (e.g. its name).
     * @param id the identificator to set
     */
    protected void setID(final String id) {
        this.id = id;
    }

    /**
     * Get identificator of the node (e.g. its name).
     * @return identificator of the node
     */
    public String getID() {
        return this.id;
    }

    @Override
    public String toString() {
        String result = id + " { ";
        if (this.getLocationAbsolute() != null) {
            result = result.concat("locationAbsolute=" + this.getLocationAbsolute().toString() + "; ");
        }
        return result.concat("}");
    }

    /**
     * Set node properties from its string representation. It's reverese operation to toString method.
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     */
    public void set(final String representation) throws NodeParsingException {
        String[] tokens = representation.split("\\s*([{=;}]\\s*)+");
        if (!tokens[0].isEmpty()) {
            this.id = tokens[0];
        }
        for (int i = 1; i < tokens.length; i += 2) {
            if (tokens[i].equalsIgnoreCase("locationAbsolute")) {
                this.setLocationAbsolute(new Vector3D(tokens[i + 1]));
            } else {
                throw new NodeParsingException("Unknown property " + tokens[i]);
            }
        }
    }

    /**
     * Check if the node has defined its absolute location.
     * @return true iff the node has defined its absolute location
     */
    public boolean isAbsolutelyLocalised() {
        return (this.getLocationAbsolute() != null) && this.getLocationAbsolute().isDefined();
    }

    /**
     * @return the locationAbsolute
     */
    public Vector3D getLocationAbsolute() {
        return locationAbsolute;
    }

    /**
     * @param locationAbsolute the locationAbsolute to set
     */
    public void setLocationAbsolute(Vector3D locationAbsolute) {
        this.locationAbsolute = locationAbsolute;
    }
}
