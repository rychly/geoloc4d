package eu.esonia.but.geoloc4d.type;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONString;

/**
 * Map of neighbouring nodes as a hash-map.
 *
 * @author rychly
 */
public final class MapOfNeighbours extends LinkedHashMap<String, NeighbourProperties> implements JSONString {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor of an empty map.
     */
    public MapOfNeighbours() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public MapOfNeighbours(final MapOfNeighbours source) {
        super();
        if (source != null) {
            this.putAll(source);
        }
    }

    /**
     * Default constructor of a map from its string representation in JSON.
     *
     * @param representation string representation of the map in JSON
     * @throws JSONException fail to parse a neighbour's string representation
     * in JSON
     */
    public MapOfNeighbours(final String representation) throws JSONException {
        this(new JSONArray(representation));
    }

    /**
     * Default constructor of a map from its representation in JSONArray.
     *
     * @param representation representation of the map in JSONArray
     * @throws JSONException fail to parse a neighbour's representation in JSON
     */
    public MapOfNeighbours(final JSONArray representation) throws JSONException {
        super();
        for (int i = 0; i < representation.length(); i++) {
            NeighbourProperties nodeProperties = new NeighbourProperties(representation.getJSONObject(i));
            this.put(nodeProperties.getID(), nodeProperties);
        }
    }

    public JSONArray toJSONArray() {
        JSONArray result = new JSONArray();
        if (!this.isEmpty()) {
            for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
                result.put(pair.getValue().toJSONObject());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        try {
            return this.toJSONArray().toString(1);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be an invalid number!", ex);
        }
    }

    @Override
    public String toJSONString() {
        return this.toJSONArray().toString();
    }

    /**
     * Set absolute locations of such neighbouring nodes which have already
     * known absolute locations in global context (in a map of all nodes).
     *
     * @param nodes the map of all nodes where the locations will be obtained
     */
    public void setLocationsFromNodes(final MapOfNodes nodes) {
        MapOfNodes localisedNodes = nodes.getNodesByLocation(true);
        for (Map.Entry<String, Node> pair : localisedNodes.entrySet()) {
            if (this.containsKey(pair.getKey())) {
                this.get(pair.getKey()).setLocationAbsolute(
                        pair.getValue().getInfo().getLocationAbsolute());
            }
        }
    }

    /**
     * Get only nodes with absolute location or relative location to reference.
     * Nodes with relative location only will have computed and set their
     * absolute location (in such case, the original nodes will not be modified,
     * i.e. result contains their copies).
     *
     * @param referenceLocation reference location (null if cannot be used in
     * computations)
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location (or computed
     * from relative)
     */
    public MapOfNeighbours getNodesWithLocation(final Vector3D referenceLocation,
            final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if (( isSetRssi && ( pair.getValue().getRssi() == null ) )
                    || ( isSetRtt && ( pair.getValue().getRtt() == null ) )) {
                // test for RSSI and RTT values
                continue;
            } else if (( pair.getValue().getLocationAbsolute() != null ) && pair.getValue().getLocationAbsolute().isDefined()) {
                // test for absolute location
                result.put(pair.getKey(), pair.getValue());
            } else if (( pair.getValue().getLocationRelative() != null ) && pair.getValue().getLocationRelative().isDefined()
                    && ( referenceLocation != null ) && referenceLocation.isDefined()) {
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
     * Nodes with relative location only will have computed and set their
     * absolute location (in such case, the original nodes will not be modified,
     * i.e. result contains their copies).
     *
     * @param referenceLocation reference location
     * @return map of nodes which have set thier absolute location (or computed
     * from relative)
     */
    public MapOfNeighbours getNodesWithLocation(final Vector3D referenceLocation) {
        return this.getNodesWithLocation(referenceLocation, false, false);
    }

    /**
     * Get only nodes with absolute location.
     *
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt resulting nodes must have set Rtt
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNeighbours getNodesWithLocation(final boolean isSetRssi, final boolean isSetRtt) {
        return this.getNodesWithLocation(null, isSetRssi, isSetRtt);
    }

    /**
     * Get only nodes with absolute location.
     *
     * @return map of nodes which have set thier absolute location
     */
    public MapOfNeighbours getNodesWithLocation() {
        return this.getNodesWithLocation(null, false, false);
    }

    /**
     * Get only nodes with set distance. Nodes with relative location only or
     * with absolute location and and defined reference location will have
     * computed and set their distance values (in such case, the original nodes
     * will not be modified, i.e. result contains their copies).
     *
     * @param referenceLocation reference location (null if cannot be used in
     * computations)
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt resulting nodes must have set Rtt
     * @return map of nodes which have set thier distance (or computed from
     * locations)
     */
    public MapOfNeighbours getNodesWithDistance(final Vector3D referenceLocation,
            final boolean isSetRssi, final boolean isSetRtt) {
        MapOfNeighbours result = new MapOfNeighbours();
        for (Map.Entry<String, NeighbourProperties> pair : this.entrySet()) {
            if (( isSetRssi && ( pair.getValue().getRssi() == null ) )
                    || ( isSetRtt && ( pair.getValue().getRtt() == null ) )) {
                // test for RSSI and RTT values
                continue;
            } else if (pair.getValue().getDistance() != null) {
                // test for distance value
                result.put(pair.getKey(), pair.getValue());
            } else if (( pair.getValue().getLocationRelative() != null ) && pair.getValue().getLocationRelative().isDefined()) {
                // test for distance of relative location from its origin
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.setDistance((Double) nodeProperties.getLocationRelative().norm());
                result.put(pair.getKey(), nodeProperties);
            } else if (( pair.getValue().getLocationAbsolute() != null ) && pair.getValue().getLocationAbsolute().isDefined()
                    && ( referenceLocation != null ) && referenceLocation.isDefined()) {
                // test for distance of absolute location to reference location
                NeighbourProperties nodeProperties = new NeighbourProperties(pair.getValue());
                nodeProperties.setDistance((Double) nodeProperties.getLocationAbsolute().distance(referenceLocation));
                result.put(pair.getKey(), nodeProperties);
            }
        }
        return result;
    }

    /**
     * Get only nodes with set distance. Nodes with relative location only or
     * with absolute location and and defined reference location will have
     * computed and set their distance values (in such case, the original nodes
     * will not be modified, i.e. result contains their copies).
     *
     * @param referenceLocation reference location (null if cannot be used in
     * computations)
     * @return map of nodes which have set thier distance (or computed from
     * locations)
     */
    public MapOfNeighbours getNodesWithDistance(final Vector3D referenceLocation) {
        return this.getNodesWithDistance(referenceLocation, false, false);
    }

    /**
     * Get only nodes with set distance.
     *
     * @param isSetRssi resulting nodes must have set Rssi
     * @param isSetRtt resulting nodes must have set Rtt
     * @return map of nodes which have set thier distance (or computed from
     * locations)
     */
    public MapOfNeighbours getNodesWithDistance(final boolean isSetRssi, final boolean isSetRtt) {
        return this.getNodesWithDistance(null, isSetRssi, isSetRtt);
    }

    /**
     * Get only nodes with set distance.
     *
     * @return map of nodes which have set thier distance (or computed from
     * locations)
     */
    public MapOfNeighbours getNodesWithDistance() {
        return this.getNodesWithDistance(null, false, false);
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node
     * with the highes distances from its neighbouring nodes).
     *
     * @return the sorted map of nodes (the most isolated nodes are last)
     */
    public MapOfNeighbours sortByIsolation() {
        return sortByIsolation(false);
    }

    /**
     * Get a map of nodes sorted by isolation (the most isolated node is a node
     * with the highes distances from its neighbouring nodes).
     *
     * @param reverseOrder true for reverse ordering of the resulting map of
     * nodes
     * @return the sorted map of nodes (the most isolated nodes are last in
     * standard order)
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
        List<IsolatedNode> isolatedNodes = new LinkedList<>();
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
     *
     * @return the sorted map of nodes (the most distant nodes are last)
     */
    public MapOfNeighbours sortByDistance() {
        return sortByDistance(false);
    }

    /**
     * Get a map of nodes sorted by distance.
     *
     * @param reverseOrder true for reverse ordering of the resulting map of
     * nodes
     * @return the sorted map of nodes (the most distant nodes are last in
     * standard order)
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
        List<NeighbourProperties> nodesByDistance = new LinkedList<>(this.values());
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
