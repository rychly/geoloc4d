package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NeighbourProperties;
import eu.esonia.but.geoloc4d.type.Node;
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
    private Double correctionFactor = 0.4;

    /**
     * Get correction factor for conversion of RTT into actual distance.
     * For better precision, should be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNeighbours)}.
     * @return the correction factor
     */
    public double getCorrectionFactor() {
        return this.correctionFactor;
    }

    @Override
    public void calibrateMetric(final MapOfNodes mapOfNodes)
            throws TrilaterationStrategyException {
        int count = 0;
        double correctionFactorSum = 0;
        // walk through mapOfNodes
        for (Node node : mapOfNodes.values()) {
            // walk through neighbours with set distance and RTT values
            for (NeighbourProperties neighbourProperties : node.scan.getNodesWithDistance(node.self.locationAbsolute, false, true).values()) {
                // for such neighbour compute the correction
                correctionFactorSum += WirelessMetric.compCorrectionFactorFromRttForDistance(
                        neighbourProperties.rtt, neighbourProperties.distance);
                count++;
            }
        }
        if (count == 0) {
            throw new TrilaterationStrategyException("Not enought nodes for this strategy! "
                    + "We need at the least one node with at the least one neighbour with set distance and RTT value.");
        } else {
            // the result is avarange form computed value
            this.correctionFactor = new Double(correctionFactorSum / count);
        }
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
