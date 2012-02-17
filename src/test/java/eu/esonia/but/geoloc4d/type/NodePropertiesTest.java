package eu.esonia.but.geoloc4d.type;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class for properties of a node as seen from its neighbouring node.
 *
 * @author rychly
 */
public class NodePropertiesTest {

    private NeighbourProperties nodeProperties;
    private static final String nodeID = "TestNode";

    public NodePropertiesTest() {
    }

    @Before
    public void setUp() {
        this.nodeProperties = new NeighbourProperties(nodeID);
        this.nodeProperties.setDistance(1.23);
        this.nodeProperties.setLocationAbsolute(new Vector3D(1.2, 3.4, 5.6));
        this.nodeProperties.setLocationRelative(new Vector3D(7.8, 9.0, 1.2));
        this.nodeProperties.setRssi((short) 123);
        this.nodeProperties.setRtt(123.123);
    }

    @After
    public void tearDown() {
        this.nodeProperties = null;
    }

    /**
     * Test of getID method, of class NeighbourProperties.
     */
    @Test
    public void testGetID() {
        assertEquals(nodeID, this.nodeProperties.getID());
    }

    /**
     * Test of toString method, of class NeighbourProperties.
     */
    @Test
    public void testToString() {
        assertEquals(nodeID + " { "
                + "distance=" + this.nodeProperties.getDistance().toString() + "; "
                + "locationAbsolute=" + this.nodeProperties.getLocationAbsolute().toString() + "; "
                + "locationRelative=" + this.nodeProperties.getLocationRelative().toString() + "; "
                + "rssi=" + this.nodeProperties.getRssi().toString() + "; "
                + "rtt=" + this.nodeProperties.getRtt().toString() + "; "
                + "}",
                this.nodeProperties.toString());
    }

    /**
     * Test of set method, of class NeighbourProperties.
     */
    @Test
    public void testSet() {
        NeighbourProperties newNodeProperties = new NeighbourProperties(nodeID);
        newNodeProperties.set(nodeID + " { "
                + "distance=" + this.nodeProperties.getDistance().toString() + "; "
                + "locationAbsolute=" + this.nodeProperties.getLocationAbsolute().toString() + "; "
                + "locationRelative=" + this.nodeProperties.getLocationRelative().toString() + "; "
                + "rssi=" + this.nodeProperties.getRssi().toString() + "; "
                + "rtt=" + this.nodeProperties.getRtt().toString() + "; "
                + "}");
        assertEquals(this.nodeProperties.toString(), newNodeProperties.toString());
    }
}
