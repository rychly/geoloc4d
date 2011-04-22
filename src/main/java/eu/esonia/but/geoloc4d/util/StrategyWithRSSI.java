package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;

/**
 * The algorithm for selection of neighbouring nodes and computation of their distanecs from RSSI.
 * @author rychly
 */
public class StrategyWithRSSI implements TrilaterationStrategy {

    /**
     * Received signal strength at 1 meter distance.
     * Have to be computed by {@link WirelessMetric#compSignalStrengthAtMeter(short, short, double, double)}, no predefined value.
     */
    private double signalStrengthAtMeter = Double.NaN;
    /**
     * Propagation constant.
     * Have to be computed by {@link WirelessMetric#compPropagationConstant(short, short, double, double)}, no predefined value.
     */
    private double propagationConstant = Double.NaN;

    /**
     * Get received signal strength at 1 meter distance for this strategy.
     * Can be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNodes)}.
     * @return the received signal strength at 1 meter distance
     */
    public double getSignalStrengthAtMeter() {
        return this.signalStrengthAtMeter;
    }

    /**
     * Get propagation constant.
     * Can be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNodes)}.
     * @return the propagation constant
     */
    public double getPropagationConstant() {
        return this.propagationConstant;
    }

    @Override
    public void calibrateMetric(final NodeData node, final MapOfNodes neighbours)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MapOfNodes selectNodesForTrilateration(final NodeData node, final MapOfNodes neighbours)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector3D doTrilateration(final MapOfNodes selectedNodes)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
