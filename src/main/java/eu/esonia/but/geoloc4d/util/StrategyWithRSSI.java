package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NeighbourProperties;
import eu.esonia.but.geoloc4d.type.Node;
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
            // if there exist at the least two such nodes, sort them by distance and for compute constants
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
            throw new TrilaterationStrategyException("Not enought nodes for this strategy! "
                    + "We need at the least one node with at the least two neighbours with set distances and RSSI values.");
        } else {
            // the result is avarange form computed values
            this.signalStrengthAtMeter = new Double(signalStrengthAtMeterSum / count);
            this.propagationConstant = new Double(propagationConstantSum / count);
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
