package eu.esonia.but.geoloc4d.type;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Map of neighbouring nodes as a hash-map.
 * @author rychly
 */
public final class MapOfNeighbours extends LinkedHashMap<String, NeighbourProperties> {

    /**
     * Default constructor of an empty map.
     */
    public MapOfNeighbours() {
        super();
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    public MapOfNeighbours(final MapOfNeighbours source) {
        this.set(source);
    }

    /**
     * Default constructor of a map from its string representation.
     * @param representation string representation of the map
     */
    public MapOfNeighbours(final String representation) {
        this.set(representation);
    }

    /**
     * Add to map content from another map.
     * @param source source scan list
     */
    public void set(final MapOfNeighbours source) {
        this.putAll(source);
    }

    /**
     * Set map from its string representation. It's reverese operation to toString method.
     * @param representation string representation of map
     */
    public void set(final String representation) {
        String[] tokens = representation.split("\\s\\+\\s");
        for (int i = 0; i < tokens.length; i++) {
            NeighbourProperties nodeProperties = new NeighbourProperties(tokens[i]);
            this.put(nodeProperties.getID(), nodeProperties);
        }
    }

    @Override
    public String toString() {
        String result = new String();
        if (!this.isEmpty()) {
            for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
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
    public MapOfNeighbours getNodesWithLocation() {
        return getNodesWithLocation(false, false);
    }

    /**
     * Get only nodes with absolute location.
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNeighbours getNodesWithLocation(final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if (pair.getValue().locationAbsolute != null
                    && (!isSetRssi || (pair.getValue().rssi != null))
                    && (!isSetRtt || (pair.getValue().rtt != null))) {
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
    public MapOfNeighbours getNodesWithLocation(final Vector3D referenceLocation) {
        return getNodesWithLocation(referenceLocation, false, false);
    }

    /**
     * Get only nodes with absolute location or relative location to reference.
     * Nodes with relative location only will have computed and set their absolute location
     * (in such case, the original nodes will not be modified, i.e. result contains their copies).
     * @param referenceLocation reference location
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location (or computed from relative)
     */
    public MapOfNeighbours getNodesWithLocation(final Vector3D referenceLocation,
            final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if ((isSetRssi && (pair.getValue().rssi == null))
                    || (isSetRtt && (pair.getValue().rtt == null))) {
                continue;
            } else if (pair.getValue().locationAbsolute != null) {
                result.put(pair.getKey(), pair.getValue());
            } else if (pair.getValue().locationRelative != null) {
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.locationAbsolute = referenceLocation.add(nodeProperties.locationRelative);
                result.put(pair.getKey(), nodeProperties);
            }
        }
        return result;
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node with the highes distances from its neighbouring nodes).
     * @return the sorted map of nodes (the most isolated nodes are first)
     */
    public MapOfNeighbours sortByIsolation() {
        return sortByIsolation(false);
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node with the highes distances from its neighbouring nodes).
     * @param reverseOrder true for reverse ordering of the resulting map of nodes
     * @return the sorted map of nodes
     */
    public MapOfNeighbours sortByIsolation(boolean reverseOrder) {
        // nested class for isolated node and its comparator
        class IsolatedNode {

            private String id;
            private NeighbourProperties nodeProperties;
            private Double isolation;

            public IsolatedNode(final String id, final NeighbourProperties nodeProperties, final Double isolation) {
                this.id = id;
                this.nodeProperties = nodeProperties;
                this.isolation = isolation;
            }
        }
        Comparator<IsolatedNode> isolationComparator = new Comparator<IsolatedNode>() {

            @Override
            public int compare(final IsolatedNode in1, final IsolatedNode in2) {
                return in1.isolation.compareTo(in2.isolation);
            }
        };
        // compute isolation values for each node and sort them
        List<IsolatedNode> isolatedNodes = new LinkedList<IsolatedNode>();
        for (Map.Entry<String, NeighbourProperties> firstNodeEntry : this.entrySet()) {
            double isolation = 0;
            for (NeighbourProperties secondNode : this.values()) {
                if (firstNodeEntry.getValue() != secondNode) {
                    isolation += firstNodeEntry.getValue().locationAbsolute.distance(
                            secondNode.locationAbsolute);
                }
            }
            isolatedNodes.add(new IsolatedNode(firstNodeEntry.getKey(), firstNodeEntry.getValue(), isolation));
        }
        Collections.sort(isolatedNodes,
                reverseOrder ? Collections.reverseOrder(isolationComparator) : isolationComparator);
        // prepare resulting map of nodes
        MapOfNeighbours result = new MapOfNeighbours();
        for (IsolatedNode isolatedNode : isolatedNodes) {
            result.put(isolatedNode.id, isolatedNode.nodeProperties);
        }
        return result;
    }
}
