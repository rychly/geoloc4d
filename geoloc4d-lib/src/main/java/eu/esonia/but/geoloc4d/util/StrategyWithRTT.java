package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.*;
import java.util.Map;

/**
 * The algorithm for selection of neighbouring nodes and computation of their
 * distanecs from RTT.
 *
 * @author rychly
 */
public class StrategyWithRTT extends TrilaterationStrategy {

    /**
     * Correction factor for conversion of RTT into actual distance. Can be
     * computed by {@link WirelessMetric#compCorrectionFactorFromRttForDistance(double, double)},
     * predefined estimated value for routed networks.
     */
    private Double correctionFactor = 0.4;

    /**
     * Get correction factor for conversion of RTT into actual distance. For
     * better precision, should be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNeighbours)}.
     *
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
            // set location of neighbours that are yet known in the map of nodes
            node.getScan().setLocationsFromNodes(mapOfNodes);
            // walk through neighbours with set distance and RTT values
            for (NeighbourProperties neighbourProperties : node.getScan().getNodesWithDistance(node.getInfo().getLocationAbsolute(), false, true).values()) {
                try {
                    // for such neighbour compute the correction
                    correctionFactorSum += WirelessMetric.compCorrectionFactorFromRttForDistance(
                            neighbourProperties.getRtt(), neighbourProperties.getDistance());
                    count++;
                }
                catch (WirelessMetricException ex) {
                    // skip the neightbouring nodes with uncomputable correction
                }
            }
        }
        if (count == 0) {
            throw new TrilaterationStrategyException("Not enought nodes for calibration of metric in this strategy! "
                    + "We need at the least one node with at the least one neighbour with set distance and RTT value.");
        } else {
            // the result is avarange form computed value
            this.correctionFactor = new Double(correctionFactorSum / count);
            // and the strategy is calibrated
            this.setAsCalibrated(true);
        }
    }

    @Override
    public MapOfNeighbours prepareNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException {
        if (this.correctionFactor == null) {
            throw new TrilaterationStrategyException("Strategy has not calibrated metric!");
        }
        MapOfNeighbours result = new MapOfNeighbours();
        // walk through mapOfNeighbours with set location and RTT
        for (Map.Entry<String, NeighbourProperties> pair : neighbours.getNodesWithLocation(node.getLocationAbsolute(), false, true).entrySet()) {
            // for each create a copy with the node's distance computed from RTT and put it into result
            NeighbourProperties neighbourWithDistance = new NeighbourProperties(pair.getValue());
            neighbourWithDistance.setDistance((Double) WirelessMetric.compDistanceFromRtt(neighbourWithDistance.getRtt(), this.correctionFactor));
            result.put(pair.getKey(), neighbourWithDistance);
        }
        // we need at the leatest four prepared nodes
        if (result.size() < 4) {
            throw new TrilaterationStrategyException("Not enought neighbouring nodes to prepare for this strategy! "
                    + "We need at the least four neighbours with set distances and RTT values.");
        } else {
            // sort to have the most distant neighbouring nodes first positions
            // idea is that with greater distance there is greater precision of RTT-to-distance transformation (RTT for near distances is too small)
            return result.sortByDistance(true);
        }
    }
}
