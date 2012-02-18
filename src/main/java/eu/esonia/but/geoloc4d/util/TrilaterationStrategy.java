package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.*;
import java.util.Arrays;

/**
 * Abstract class of algorithms for selection of neighbouring nodes and
 * computation of their distanecs for trilateration. Strategy design pattern.
 *
 * @author rychly
 */
public abstract class TrilaterationStrategy {

    /**
     * Indicates the strategy with calibrated metric.
     */
    private boolean calibrated = false;

    /**
     * Calibrate metric used by the trilateration strategy (e.g. transformation
     * of RSSI/RTT to distance) for a map of reference nodes.
     *
     * @param mapOfNodes the map of reference nodes
     * @throws TrilaterationStrategyException in case of incomplete data for the
     * calibration
     */
    public abstract void calibrateMetric(final MapOfNodes mapOfNodes)
            throws TrilaterationStrategyException;

    /**
     * Check if the strategy is calibrated, i.e. {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.MapOfNodes)}
     * has been executed before or the strategy do not need calibration.
     *
     * @return true iff the strategy is calibrated
     */
    public boolean isCalibrated() {
        return this.calibrated;
    }

    /**
     * Set the strategy as calibrated or not. It is used by {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.MapOfNodes)}
     * of strategy implementations.
     *
     * @param calibrated true to set the strategy as calibrated
     */
    protected void setAsCalibrated(boolean calibrated) {
        this.calibrated = calibrated;
    }

    /**
     * Prepare a group of nodes suitable for the trilateration strategy for a
     * reference node and its neighbours. There can be different approaches for
     * RSSI/RTT/distance based strategies.
     *
     * @param node the reference node
     * @param neighbours the neighbours of the reference node
     * @return the prepared group of nodes (as a map)
     * @throws TrilaterationStrategyException in case of incomplete data for the
     * further trilateration
     */
    public abstract MapOfNeighbours prepareNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException;

    /**
     * Perform the trilateration according to the specific strategy and prepared
     * nodes.
     *
     * @param preparedNodes node used for the trilateration
     * @return result of the trilateration, i.e. location of blind node
     * @throws TrilaterationStrategyException in case of incomplete data for the
     * trilateration
     */
    public Vector3D doTrilateration(final MapOfNeighbours preparedNodes)
            throws TrilaterationStrategyException {
        NeighbourProperties nodes[] = Arrays.copyOf(preparedNodes.values().toArray(), preparedNodes.size(), NeighbourProperties[].class);
        // we need at the leatest four prepared nodes
        if (( nodes.length < 4 )
                || ( nodes[0].getLocationAbsolute() == null ) || ( nodes[0].getLocationAbsolute().isUndefined() )
                || ( nodes[0].getDistance() == null )
                || ( nodes[1].getLocationAbsolute() == null ) || ( nodes[1].getLocationAbsolute().isUndefined() )
                || ( nodes[1].getDistance() == null )
                || ( nodes[2].getLocationAbsolute() == null ) || ( nodes[2].getLocationAbsolute().isUndefined() )
                || ( nodes[2].getDistance() == null )
                || ( nodes[3].getLocationAbsolute() == null ) || ( nodes[3].getLocationAbsolute().isUndefined() )
                || ( nodes[3].getDistance() == null )) {
            throw new TrilaterationStrategyException("Not enought neighbouring nodes for 3D trilateration! "
                    + "We need at the least four prepared nodes with absolute locations and set distances.");
        }
        // perform 3D trilateration
        return WirelessMetric.trilateration3D(
                nodes[0].getLocationAbsolute(), nodes[0].getDistance(), nodes[1].getLocationAbsolute(), nodes[1].getDistance(), nodes[2].getLocationAbsolute(), nodes[2].getDistance(), nodes[3].getLocationAbsolute(), nodes[3].getDistance());
    }
}
