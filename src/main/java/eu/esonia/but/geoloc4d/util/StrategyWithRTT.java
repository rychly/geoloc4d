package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;

/**
 * The algorithm for selection of neighbouring nodes and computation of their distanecs from RTT.
 * @author rychly
 */
public class StrategyWithRTT implements TrilaterationStrategy {

    /**
     * Correction factor for conversion of RTT into actual distance.
     * Can be computed by {@link WirelessMetric#compCorrectionFactorFromRttForDistance(double, double)}, predefined estimated value for routed networks.
     */
    private double correctionFactor = 0.4;

    /**
     * Get correction factor for conversion of RTT into actual distance.
     * For better precision, should be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNodes)}.
     * @return the correction factor
     */
    public double getCorrectionFactor() {
        return this.correctionFactor;
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
