package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import java.util.Collection;

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
     * For better precision, should be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNeighbours)}.
     * @return the correction factor
     */
    public double getCorrectionFactor() {
        return this.correctionFactor;
    }

    @Override
    public void calibrateMetric(Collection<Node> nodes)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MapOfNeighbours selectNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector3D doTrilateration(final MapOfNeighbours selectedNodes)
            throws TrilaterationStrategyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
