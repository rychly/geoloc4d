package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import java.util.Collection;

/**
 * Interface of algorithms for selection of neighbouring nodes and computation of their distanecs for trilateration.
 * Strategy design pattern.
 * @author rychly
 */
public interface TrilaterationStrategy {

    /**
     * To create new instances of specific trilateration strategies.
     * Factory design pattern.
     */
    public static final class Factory {

        /**
         * Make a new instance of a specific trilateration strategy based on RSSI.
         * @return the trilateration strategy
         */
        public static TrilaterationStrategy newStrategyWithRSSI() {
            return new StrategyWithRSSI();
        }

        /**
         * Make a new instance of a specific trilateration strategy based on RTT.
         * @return the trilateration strategy
         */
        public static TrilaterationStrategy newStrategyWithRTT() {
            return new StrategyWithRTT();
        }
    }

    /**
     * Calibrate metric used by the trilateration strategy (e.g. transformation of RSSI/RTT to distance) for a collection of reference nodes.
     * @param nodes the collection of reference nodes
     * @throws TrilaterationStrategyException in case of incomplete data for the calibration
     */
    public abstract void calibrateMetric(Collection<Node> nodes)
            throws TrilaterationStrategyException;

    /**
     * Select a group of nodes suitable for the trilateration strategy for a reference node and its neighbours.
     * There can be different approaches for RSSI/RTT/distance based strategies.
     * @param node the reference node
     * @param neighbours the neighbours of the reference node
     * @return the group of selected nodes (as a map)
     * @throws TrilaterationStrategyException in case of incomplete data for the further trilateration
     */
    public MapOfNeighbours selectNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException;

    /**
     * Perform the trilateration according to the specific strategy and selected nodes.
     * @param selectedNodes node used for the trilateration
     * @return result of the trilateration, i.e. location of blind node
     * @throws TrilaterationStrategyException in case of incomplete data for the trilateration
     */
    public Vector3D doTrilateration(final MapOfNeighbours selectedNodes)
            throws TrilaterationStrategyException;
}
