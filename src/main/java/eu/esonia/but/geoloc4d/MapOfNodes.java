package eu.esonia.but.geoloc4d;

import java.util.HashMap;
import java.util.Map;

/**
 * Map of nodes as a hash-map.
 * @author rychly
 */
public final class MapOfNodes extends HashMap<String, NodeProperties> {

    /**
     * Default constructor of an empty map.
     */
    MapOfNodes() {
        super();
    }

    /**
     * Default constructor of a map from its string representation.
     * @param representation string representation of the map
     */
    MapOfNodes(final String representation) {
        this.set(representation);
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    MapOfNodes(final MapOfNodes source) {
        this.set(source);
    }

    /**
     * Add to map content from another map.
     * @param source source scan list
     */
    public void set(final MapOfNodes source) {
        this.putAll(source);
    }

    /**
     * Set map from its string representation. It's reverese operation to toString method.
     * @param representation string representation of map
     */
    public void set(final String representation) {
        String[] tokens = representation.split("\\s\\+\\s");
        for (int i = 0; i < tokens.length; i++) {
            NodeProperties nodeProperties = new NodeProperties(tokens[i]);
            this.put(nodeProperties.getID(), nodeProperties);
        }
    }

    @Override
    public String toString() {
        String result = new String();
        if (!this.isEmpty()) {
            for (Map.Entry<String, NodeProperties> pair : this.entrySet()) {
                result = result.concat(pair.getValue() + " + ");
            }
            result = result.substring(0, result.length() - 3);
        }
        return result;
    }

    /**
     * Get only nodes with absolute location.
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNodes getNodesWithLocation() {
        MapOfNodes result = new MapOfNodes();
        for (Map.Entry<String, NodeProperties> pair : this.entrySet()) {
            if (pair.getValue().locationAbsolute != null) {
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return result;
    }

    /**
     * Get only nodes with absolute location or relative location to reference.
     * Nodes with relative location only will have computed and set their absolute location
     * (in such case, the original nodes will not be modified, i.e. result contains their copies).
     * @param referenceLocation reference location
     * @return map of nodes which have set thier absolute location (or computed from relative)
     */
    public MapOfNodes getNodesWithLocation(final Vector3D referenceLocation) {
        MapOfNodes result = new MapOfNodes();
        for (Map.Entry<String, NodeProperties> pair : this.entrySet()) {
            if (pair.getValue().locationAbsolute != null) {
                result.put(pair.getKey(), pair.getValue());
            } else if (pair.getValue().locationRelative != null) {
                NodeProperties nodeProperties = new NodeProperties(pair.getValue());
                nodeProperties.locationAbsolute = referenceLocation.add(nodeProperties.locationRelative);
                result.put(pair.getKey(), nodeProperties);
            }
        }
        return result;
    }
}
