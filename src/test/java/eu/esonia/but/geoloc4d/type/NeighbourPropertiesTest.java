package eu.esonia.but.geoloc4d.type;

import org.json.JSONException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class for properties of a node as seen from its neighbouring node.
 *
 * @author rychly
 */
public class NeighbourPropertiesTest {

    private NeighbourProperties nodeProperties;
    private static final String nodeID = "TestNode";

    public NeighbourPropertiesTest() {
    }

    @Before
    public void setUp() {
        this.nodeProperties = new NeighbourProperties();
        this.nodeProperties.setID(nodeID);
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
     *
     * @throws JSONException unable to parse toString result as a JSON
     * representation of the map
     */
    @Test
    public void testToString() throws JSONException {
        NeighbourProperties newNodeProperties = new NeighbourProperties(this.nodeProperties.toString());
        assertEquals(this.nodeProperties, newNodeProperties);
    }

    /**
     * Test of toJSONObject method, of class NeighbourProperties.
     *
     * @throws JSONException fail to parse the properties in JSON
     */
    @Test
    public void testToJSONObject() throws JSONException {
        NeighbourProperties newNodeProperties = new NeighbourProperties(this.nodeProperties.toJSONObject());
        assertEquals(this.nodeProperties, newNodeProperties);
    }
}
