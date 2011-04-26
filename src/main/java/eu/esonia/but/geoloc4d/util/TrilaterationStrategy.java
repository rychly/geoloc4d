package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NeighbourProperties;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;

/**
 * Abstract class of algorithms for selection of neighbouring nodes and computation of their distanecs for trilateration.
 * Strategy design pattern.
 * @author rychly
 */
public abstract class TrilaterationStrategy {

    /**
     * Calibrate metric used by the trilateration strategy (e.g. transformation of RSSI/RTT to distance) for a map of reference nodes.
     * @param mapOfNodes the map of reference nodes
     * @throws TrilaterationStrategyException in case of incomplete data for the calibration
     */
    public abstract void calibrateMetric(final MapOfNodes mapOfNodes)
            throws TrilaterationStrategyException;

    /**
     * Prepare a group of nodes suitable for the trilateration strategy for a reference node and its neighbours.
     * There can be different approaches for RSSI/RTT/distance based strategies.
     * @param node the reference node
     * @param neighbours the neighbours of the reference node
     * @return the prepared group of nodes (as a map)
     * @throws TrilaterationStrategyException in case of incomplete data for the further trilateration
     */
    public abstract MapOfNeighbours prepareNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException;

    /**
     * Perform the trilateration according to the specific strategy and prepared nodes.
     * @param preparedNodes node used for the trilateration
     * @return result of the trilateration, i.e. location of blind node
     * @throws TrilaterationStrategyException in case of incomplete data for the trilateration
     */
    public Vector3D doTrilateration(final MapOfNeighbours preparedNodes)
            throws TrilaterationStrategyException {
        NeighbourProperties nodes[] = (NeighbourProperties[]) preparedNodes.values().toArray();
        // we need at the leatest four prepared nodes
        if ((nodes.length < 4)
                || (nodes[0].locationAbsolute == null) || (nodes[0].locationAbsolute.isUndefined())
                || (nodes[0].distance == null)
                || (nodes[1].locationAbsolute == null) || (nodes[1].locationAbsolute.isUndefined())
                || (nodes[1].distance == null)
                || (nodes[2].locationAbsolute == null) || (nodes[2].locationAbsolute.isUndefined())
                || (nodes[2].distance == null)
                || (nodes[3].locationAbsolute == null) || (nodes[3].locationAbsolute.isUndefined())
                || (nodes[3].distance == null)) {
            throw new TrilaterationStrategyException("Not enought neighbouring nodes for 3D trilateration! "
                    + "We need at the least four prepared nodes with absolute locations and set distances.");
        }
        // perform 3D trilateration
        return WirelessMetric.trilateration3D(
                nodes[0].locationAbsolute, nodes[0].distance,
                nodes[1].locationAbsolute, nodes[1].distance,
                nodes[2].locationAbsolute, nodes[2].distance,
                nodes[3].locationAbsolute, nodes[3].distance);
    }
}
