package eu.esonia.but.geoloc4d;

import junit.framework.TestCase;

/**
 * Test of class for properties of a node as seen from its neighbouring node.
 * @author rychly
 */
public class NodePropertiesTest extends TestCase {

    private NodeProperties nodeProperties;
    private static final String nodeID = "TestNode";

    public NodePropertiesTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        this.nodeProperties = new NodeProperties(nodeID);
        this.nodeProperties.distance = 1.23;
        this.nodeProperties.locationAbsolute = new Vector3D(1.2, 3.4, 5.6);
        this.nodeProperties.locationRelative = new Vector3D(7.8, 9.0, 1.2);
        this.nodeProperties.rssi = 123;
        this.nodeProperties.rtt = 123.123;
    }

    @Override
    protected void tearDown() throws Exception {
        this.nodeProperties = null;
    }

    /**
     * Test of getID method, of class NodeProperties.
     */
    public void testGetID() {
        assertEquals(this.nodeProperties.getID(), nodeID);
    }

    /**
     * Test of toString method, of class NodeProperties.
     */
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
     * Test of set method, of class NodeProperties.
     */
    public void testSet() {
        NodeProperties newNodeProperties = new NodeProperties(nodeID);
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
