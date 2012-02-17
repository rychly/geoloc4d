package eu.esonia.but.geoloc4d.type;

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
    public NodeData self;
    /**
     * Data about neighbouring nodes (from scan).
     */
    public MapOfNeighbours scan;

    public Node(final String representation) {
        this.self = new NodeData(representation);
        this.scan = new MapOfNeighbours();
    }

    public Node(final String id, final String representation) {
        this.self = new NodeData(id, representation);
        this.scan = new MapOfNeighbours();
    }

    public Node(final JSONObject representation) throws NodeParsingException, JSONException {
        this.set(representation);
    }

    public Node(final NodeData self, final MapOfNeighbours scan) {
        this.self = new NodeData(self);
        this.scan = new MapOfNeighbours(scan);
    }

    @Override
    public String toString() {
        String id = self.getID();
        return id + "=" + self.toString().substring(id.length() + 1) + "\n"
                + id + ".scan=" + scan.toString();
    }

    @Override
    public String toJSONString() {
        JSONObject result = new JSONObject();
        try {
            result.put("self", (JSONString) self);
            result.put("scan", (JSONString) scan);
            return result.toString();
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }

    public void set(final JSONObject representation) throws NodeParsingException, JSONException {
        if (!representation.has("self") || !representation.has("scan")) {
            throw new NodeParsingException("The representation of a node must have 'self' and 'scan' attributes!");
        }
        this.self = new NodeData(representation.getJSONObject("self"));
        this.scan = new MapOfNeighbours(representation.getJSONArray("scan"));
    }
}
