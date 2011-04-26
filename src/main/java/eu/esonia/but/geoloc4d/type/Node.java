package eu.esonia.but.geoloc4d.type;

/**
 * Node of a network with geolocation ability.
 * @author rychly
 */
public class Node {

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

    @Override
    public String toString() {
        String id = self.getID();
        return id + "=" + self.toString().substring(id.length() + 1) + "\n"
                + id + ".scan=" + scan.toString();
    }
}
