package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NeighbourProperties;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import java.util.Map;

/**
 * The algorithm for selection of neighbouring nodes and computation of their distanecs from RSSI.
 * @author rychly
 */
public class StrategyWithRSSI extends TrilaterationStrategy {

    /**
     * Received signal strength at 1 meter distance.
     * Have to be computed by {@link WirelessMetric#compSignalStrengthAtMeter(short, short, double, double)}, no predefined value.
     */
    private Double signalStrengthAtMeter = null;
    /**
     * Propagation constant.
     * Have to be computed by {@link WirelessMetric#compPropagationConstant(short, short, double, double)}, no predefined value.
     */
    private Double propagationConstant = null;

    /**
     * Get received signal strength at 1 meter distance for this strategy.
     * Can be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNeighbours)}.
     * @return the received signal strength at 1 meter distance
     */
    public double getSignalStrengthAtMeter() {
        return this.signalStrengthAtMeter;
    }

    /**
     * Get propagation constant.
     * Can be used after {@link #calibrateMetric(eu.esonia.but.geoloc4d.type.NodeData, eu.esonia.but.geoloc4d.type.MapOfNeighbours)}.
     * @return the propagation constant
     */
    public double getPropagationConstant() {
        return this.propagationConstant;
    }

    @Override
    public void calibrateMetric(final MapOfNodes mapOfNodes)
            throws TrilaterationStrategyException {
        int count = 0;
        double signalStrengthAtMeterSum = 0;
        double propagationConstantSum = 0;
        // walk through mapOfNodes
        for (Node node : mapOfNodes.values()) {
            // select neighbours with set distance and RSSI values
            MapOfNeighbours mapOfNeighbours = node.scan.getNodesWithDistance(node.self.locationAbsolute, true, false);
            // if there exist at the least two such nodes, sort them by distance (the most distant are last) and for compute constants from two most closed
            if (mapOfNeighbours.size() >= 2) {
                NeighbourProperties arrayOfNeighbours[] = (NeighbourProperties[]) mapOfNeighbours.sortByDistance().values().toArray();
                signalStrengthAtMeterSum += WirelessMetric.compSignalStrengthAtMeter(
                        arrayOfNeighbours[0].rssi, arrayOfNeighbours[1].rssi, arrayOfNeighbours[0].distance, arrayOfNeighbours[1].distance);
                propagationConstantSum += WirelessMetric.compPropagationConstant(
                        arrayOfNeighbours[0].rssi, arrayOfNeighbours[1].rssi, arrayOfNeighbours[0].distance, arrayOfNeighbours[1].distance);
                count++;
            }
        }
        if (count == 0) {
            throw new TrilaterationStrategyException("Not enought nodes for calibration of metric in this strategy! "
                    + "We need at the least one node with at the least two neighbours with set distances and RSSI values.");
        } else {
            // the result is avarange form computed values
            this.signalStrengthAtMeter = new Double(signalStrengthAtMeterSum / count);
            this.propagationConstant = new Double(propagationConstantSum / count);
        }
    }

    @Override
    public MapOfNeighbours prepareNodesForTrilateration(final NodeData node, final MapOfNeighbours neighbours)
            throws TrilaterationStrategyException {
        if ((this.signalStrengthAtMeter == null) || (this.propagationConstant == null)) {
            throw new TrilaterationStrategyException("Strategy has not calibrated metric!");
        }
        MapOfNeighbours result = new MapOfNeighbours();
        // walk through mapOfNeighbours with set location and RSSI
        for (Map.Entry<String, NeighbourProperties> pair : neighbours.getNodesWithLocation(node.locationAbsolute, true, false).entrySet()) {
            // for each create a copy with the node's distance computed from RSSI and put it into result
            NeighbourProperties neighbourWithDistance = new NeighbourProperties(pair.getValue());
            neighbourWithDistance.distance = WirelessMetric.compDistanceFromRssi(
                    neighbourWithDistance.rssi, this.signalStrengthAtMeter, this.propagationConstant);
            result.put(pair.getKey(), neighbourWithDistance);
        }
        // we need at the leatest four prepared nodes
        if (result.size() < 4) {
            throw new TrilaterationStrategyException("Not enought neighbouring nodes to prepare for this strategy! "
                    + "We need at the least four neighbours with set distances and RSSI values.");
        } else {
            // sort to have neighbouring nodes forming cluster at first positions (the most isolated nodes are last)
            // idea is that RSSI of neighbours is similar location (i.e. similar direction from blind node) will be affected similarly by its environment
            return result.sortByIsolation();
        }
    }
}
