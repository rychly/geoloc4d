package eu.esonia.but.geoloc4d.type;

import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * Basic data of a node.
 *
 * @author rychly
 */
public class NodeData implements JSONString {

    /**
     * Identificator of the node (e.g. its name).
     */
    private String id;
    /**
     * Absolute location of the node.
     */
    private Vector3D locationAbsolute;

    protected NodeData() {
        // empty constructor for an initiation from subclasses
    }

    /**
     * Constructor of a node from its string representation in JSON.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the string representation in JSON
     */
    public NodeData(final String representation) throws NodeParsingException, JSONException {
        this(new JSONObject(representation));
    }

    /**
     * Constructor of a node from its representation as JSONObject.
     *
     * @param representation representation of the node properties in JSONObject
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the representation in JSON
     */
    public NodeData(final JSONObject representation) throws NodeParsingException, JSONException {
        if (representation.length() != 1) {
            throw new NodeParsingException("Unset or multiple ID!");
        }
        this.setID((String) representation.keys().next());
        JSONObject properties = representation.getJSONObject(this.getID());
        if (properties.has("locationAbsolute")) {
            this.setLocationAbsolute(new Vector3D(properties.getJSONArray("locationAbsolute")));
        }
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public NodeData(final NodeData source) {
        this.id = source.getID();
        this.locationAbsolute = ( source.getLocationAbsolute() != null )
                ? new Vector3D(source.getLocationAbsolute()) : null;
    }

    /**
     * Set identificator of the node (e.g. its name).
     *
     * @param id the identificator to set
     */
    protected final void setID(final String id) {
        this.id = id;
    }

    /**
     * Get identificator of the node (e.g. its name).
     *
     * @return identificator of the node
     */
    public final String getID() {
        return this.id;
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject resultProps = new JSONObject();
            if (this.getLocationAbsolute() != null) {
                resultProps.put("locationAbsolute", this.getLocationAbsolute().toJSONArray());
            }
            return new JSONObject().put(this.getID(), resultProps);
            // e.g. "SecondNode":{"locationAbsolute":[0,1000,0]}
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }

    @Override
    public String toString() {
        try {
            return this.toJSONObject().toString(1);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be an invalid number!", ex);
        }
    }

    @Override
    public String toJSONString() {
        return this.toJSONObject().toString();
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
                    && ( ( this.getLocationAbsolute() == null && neighbourProperties.getLocationAbsolute() == null ) || this.getLocationAbsolute().equals(neighbourProperties.getLocationAbsolute()) );
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getID());
        hash = 97 * hash + Objects.hashCode(this.getLocationAbsolute());
        return hash;
    }

    /**
     * Check if the node has defined its absolute location.
     *
     * @return true iff the node has defined its absolute location
     */
    public final boolean isAbsolutelyLocalised() {
        return ( this.getLocationAbsolute() != null ) && this.getLocationAbsolute().isDefined();
    }

    /**
     * @return the locationAbsolute
     */
    public final Vector3D getLocationAbsolute() {
        return locationAbsolute;
    }

    /**
     * @param locationAbsolute the locationAbsolute to set
     */
    public final void setLocationAbsolute(Vector3D locationAbsolute) {
        this.locationAbsolute = locationAbsolute;
    }
}
