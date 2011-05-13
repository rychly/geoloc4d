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

    private static final long serialVersionUID = 1L;

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
        super();
        this.set(source);
    }

    /**
     * Default constructor of a map from its string representation.
     * @param representation string representation of the map
     * @throws NodeParsingException fail to parse the string representation
     */
    public MapOfNeighbours(final String representation) throws NodeParsingException {
        super();
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
     * @throws NodeParsingException fail to parse the string representation
     */
    public void set(final String representation) throws NodeParsingException {
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
     * Get only nodes with absolute location or relative location to reference.
     * Nodes with relative location only will have computed and set their absolute location
     * (in such case, the original nodes will not be modified, i.e. result contains their copies).
     * @param referenceLocation reference location (null if cannot be used in computations)
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location (or computed from relative)
     */
    public MapOfNeighbours getNodesWithLocation(final Vector3D referenceLocation,
            final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if ((isSetRssi && (pair.getValue().getRssi() == null))
                    || (isSetRtt && (pair.getValue().getRtt() == null))) {
                // test for RSSI and RTT values
                continue;
            } else if ((pair.getValue().getLocationAbsolute() != null) && pair.getValue().getLocationAbsolute().isDefined()) {
                // test for absolute location
                result.put(pair.getKey(), pair.getValue());
            } else if ((pair.getValue().getLocationRelative() != null) && pair.getValue().getLocationRelative().isDefined()
                    && (referenceLocation != null) && referenceLocation.isDefined()) {
                // test for absolute location as relative from reference location
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.setLocationAbsolute(referenceLocation.add(nodeProperties.getLocationRelative()));
                result.put(pair.getKey(), nodeProperties);
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
        return this.getNodesWithLocation(referenceLocation, false, false);
    }

    /**
     * Get only nodes with absolute location.
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNeighbours getNodesWithLocation(final boolean isSetRssi, final boolean isSetRtt) {
        return this.getNodesWithLocation(null, isSetRssi, isSetRtt);
    }

    /**
     * Get only nodes with absolute location.
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNeighbours getNodesWithLocation() {
        return this.getNodesWithLocation(null, false, false);
    }

    /**
     * Get only nodes with set distance.
     * Nodes with relative location only or with absolute location and and defined reference location will have computed and set their distance values
     * (in such case, the original nodes will not be modified, i.e. result contains their copies).
     * @param referenceLocation reference location (null if cannot be used in computations)
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier distance (or computed from locations)
     */
    public MapOfNeighbours getNodesWithDistance(final Vector3D referenceLocation,
            final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if ((isSetRssi && (pair.getValue().getRssi() == null))
                    || (isSetRtt && (pair.getValue().getRtt() == null))) {
                // test for RSSI and RTT values
                continue;
            } else if (pair.getValue().getDistance() != null) {
                // test for distance value
                result.put(pair.getKey(), pair.getValue());
            } else if ((pair.getValue().getLocationRelative() != null) && pair.getValue().getLocationRelative().isDefined()) {
                // test for distance of relative location from its origin
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.setDistance((Double) nodeProperties.getLocationRelative().norm());
                result.put(pair.getKey(), nodeProperties);
            } else if ((pair.getValue().getLocationAbsolute() != null) && pair.getValue().getLocationAbsolute().isDefined()
                    && (referenceLocation != null) && referenceLocation.isDefined()) {
                // test for distance of absolute location to reference location
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.setDistance((Double) nodeProperties.getLocationAbsolute().distance(referenceLocation));
                result.put(pair.getKey(), nodeProperties);
            }
        }
        return result;
    }

    /**
     * Get only nodes with set distance.
     * Nodes with relative location only or with absolute location and and defined reference location will have computed and set their distance values
     * (in such case, the original nodes will not be modified, i.e. result contains their copies).
     * @param referenceLocation reference location (null if cannot be used in computations)
     * @return map of nodes which have set thier distance (or computed from locations)
     */
    public MapOfNeighbours getNodesWithDistance(final Vector3D referenceLocation) {
        return this.getNodesWithDistance(referenceLocation, false, false);
    }

    /**
     * Get only nodes with set distance.
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt  resulting nodes must have set Rtt
     * @return map of nodes which have set thier distance (or computed from locations)
     */
    public MapOfNeighbours getNodesWithDistance(final boolean isSetRssi, final boolean isSetRtt) {
        return this.getNodesWithDistance(null, isSetRssi, isSetRtt);
    }

    /**
     * Get only nodes with set distance.
     * @return map of nodes which have set thier distance (or computed from locations)
     */
    public MapOfNeighbours getNodesWithDistance() {
        return this.getNodesWithDistance(null, false, false);
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node with the highes distances from its neighbouring nodes).
     * @return the sorted map of nodes (the most isolated nodes are last)
     */
    public MapOfNeighbours sortByIsolation() {
        return sortByIsolation(false);
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node with the highes distances from its neighbouring nodes).
     * @param reverseOrder true for reverse ordering of the resulting map of nodes
     * @return the sorted map of nodes (the most isolated nodes are last in standard order)
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
                    isolation += firstNodeEntry.getValue().getLocationAbsolute().distance(
                            secondNode.getLocationAbsolute());
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

    /**
     * Get a map of nodes sorted by distance.
     * @return the sorted map of nodes (the most distant nodes are last)
     */
    public MapOfNeighbours sortByDistance() {
        return sortByDistance(false);
    }

    /**
     * Get a map of nodes sorted by distance.
     * @param reverseOrder true for reverse ordering of the resulting map of nodes
     * @return the sorted map of nodes (the most distant nodes are last in standard order)
     */
    public MapOfNeighbours sortByDistance(boolean reverseOrder) {
        // nested class for comparator
        Comparator<NeighbourProperties> distanceComparator = new Comparator<NeighbourProperties>() {

            @Override
            public int compare(final NeighbourProperties np1, final NeighbourProperties np2) {
                return np1.getDistance().compareTo(np2.getDistance());
            }
        };
        // sort list of nodes from the map of nodes
        List<NeighbourProperties> nodesByDistance = new LinkedList<NeighbourProperties>(this.values());
        Collections.sort(nodesByDistance,
                reverseOrder ? Collections.reverseOrder(distanceComparator) : distanceComparator);
        // prepare resulting map of nodes
        MapOfNeighbours result = new MapOfNeighbours();
        for (NeighbourProperties nodeProperties : nodesByDistance) {
            result.put(nodeProperties.getID(), nodeProperties);
        }
        return result;
    }
}
