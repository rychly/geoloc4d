package eu.esonia.but.geoloc4d.type;

import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * Node of a network with geolocation ability.
 *
 * @author rychly
 */
public class Node implements JSONString {

    /**
     * Data about the node (itself).
     */
    private NodeData info;
    /**
     * Data about neighbouring nodes (from scan).
     */
    private MapOfNeighbours scan;

    /**
     * Constructor of a node from its string representation in JSON.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the string representation in JSON
     */
    public Node(final String representation) throws NodeParsingException, JSONException {
        this(new JSONObject(representation));
    }

    /**
     * Constructor of a node from its representation as JSONObject.
     *
     * @param representation representation of the node properties in JSONObject
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the representation in JSON
     */
    public Node(final JSONObject representation) throws NodeParsingException, JSONException {
        if (!representation.has("info") || !representation.has("scan")) {
            throw new NodeParsingException("The representation of a node must have 'info' and 'scan' attributes!");
        }
        this.info = new NodeData(representation.getJSONObject("info"));
        this.scan = new MapOfNeighbours(representation.getJSONArray("scan"));
    }

    /**
     * Constructor of a node from its components.
     *
     * @param info the NodeData component
     * @param scan the MapOfNeighbours component
     */
    public Node(final NodeData info, final MapOfNeighbours scan) {
        this.info = new NodeData(info);
        this.scan = new MapOfNeighbours(scan);
    }

    /**
     * @return the NodeData component (info)
     */
    public NodeData getInfo() {
        return info;
    }

    /**
     * @return the MapOfNeighbours component (scan)
     */
    public MapOfNeighbours getScan() {
        return scan;
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            result.put("info", this.getInfo().toJSONObject());
            result.put("scan", this.getScan().toJSONArray());
            return result;
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
        } else if (!( object instanceof Node )) {
            return false;
        } else {
            Node node = (Node) object;
            return ( ( this.getInfo() == null && node.getInfo() == null ) || this.getInfo().equals(node.getInfo()) )
                    && ( ( this.getScan() == null && node.getScan() == null ) || this.getScan().equals(node.getScan()) );
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.getInfo());
        hash = 29 * hash + Objects.hashCode(this.getScan());
        return hash;
    }
}
