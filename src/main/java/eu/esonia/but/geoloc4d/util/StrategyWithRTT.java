package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;

/**
 * The algorithm for selection of neighbouring nodes and computation of their distanecs from RTT.
 * @author rychly
 */
public class StrategyWithRTT implements TrilaterationStrategy {

    @Override
    public TrilaterationStrategy makeTrilaterationStrategy() {
        throw new UnsupportedOperationException("Not supported yet.");
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
