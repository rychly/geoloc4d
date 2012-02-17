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
            result.put("self", self);
            result.put("scan", scan);
            return new JSONObject().put(self.getID(), result).toString();
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }
}
