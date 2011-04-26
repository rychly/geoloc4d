package eu.esonia.but.geoloc4d.type;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of class for properties of a node as seen from its neighbouring node.
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
        this.nodeProperties.distance = 1.23;
        this.nodeProperties.locationAbsolute = new Vector3D(1.2, 3.4, 5.6);
        this.nodeProperties.locationRelative = new Vector3D(7.8, 9.0, 1.2);
        this.nodeProperties.rssi = 123;
        this.nodeProperties.rtt = 123.123;
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
        assertEquals(this.nodeProperties.getID(), nodeID);
    }

    /**
     * Test of toString method, of class NeighbourProperties.
     */
    @Test
    public void testToString() {
        assertEquals(this.nodeProperties.toString(), nodeID + " { "
                + "distance=" + this.nodeProperties.distance.toString() + "; "
                + "locationAbsolute=" + this.nodeProperties.locationAbsolute.toString() + "; "
                + "locationRelative=" + this.nodeProperties.locationRelative.toString() + "; "
                + "rssi=" + this.nodeProperties.rssi.toString() + "; "
                + "rtt=" + this.nodeProperties.rtt.toString() + "; "
                + "}");
    }

    /**
     * Test of set method, of class NeighbourProperties.
     */
    @Test
    public void testSet() {
        NeighbourProperties newNodeProperties = new NeighbourProperties(nodeID);
        newNodeProperties.set(nodeID + " { "
                + "distance=" + this.nodeProperties.distance.toString() + "; "
                + "locationAbsolute=" + this.nodeProperties.locationAbsolute.toString() + "; "
                + "locationRelative=" + this.nodeProperties.locationRelative.toString() + "; "
                + "rssi=" + this.nodeProperties.rssi.toString() + "; "
                + "rtt=" + this.nodeProperties.rtt.toString() + "; "
                + "}");
        assertEquals(this.nodeProperties.toString(), newNodeProperties.toString());
    }
}
