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
    public Vector3D locationAbsolute;

    /**
     * Constructor of a node from its identificator and string representation.
     * @param id identificator of the node properties
     * @param representation string representation of the node properties
     * @throws NodeDataException fail to parse the string representation
     */
    public NodeData(final String id, final String representation) throws NodeDataException {
        this.set(representation);
        this.id = id;
    }

    /**
     * Constructor of a node from its string representation.
     * @param representation string representation of the node properties
     * @throws NodeDataException fail to parse the string representation
     */
    public NodeData(final String representation) throws NodeDataException {
        this.set(representation);
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    public NodeData(final NodeData source) {
        this.id = source.getID();
        this.locationAbsolute = (source.locationAbsolute != null)
                ? new Vector3D(source.locationAbsolute) : null;
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
        if (this.locationAbsolute != null) {
            result = result.concat("locationAbsolute=" + this.locationAbsolute.toString() + "; ");
        }
        return result.concat("}");
    }

    /**
     * Set node properties from its string representation. It's reverese operation to toString method.
     * @param representation string representation of the node properties
     * @throws NodeDataException fail to parse the string representation
     */
    public void set(final String representation) throws NodeDataException {
        String[] tokens = representation.split("\\s*([{=;}]\\s*)+");
        if (!tokens[0].isEmpty()) {
            this.id = tokens[0];
        }
        for (int i = 1; i < tokens.length; i += 2) {
            if (tokens[i].equalsIgnoreCase("locationAbsolute")) {
                this.locationAbsolute = new Vector3D(tokens[i + 1]);
            } else {
                throw new NodeDataException("Unknown property " + tokens[i]);
            }
        }
    }
}
