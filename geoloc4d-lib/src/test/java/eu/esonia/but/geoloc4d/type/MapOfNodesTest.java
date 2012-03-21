package eu.esonia.but.geoloc4d.type;

import org.json.JSONException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class for map of nodes as a hash-map.
 *
 * @author rychly
 */
public class MapOfNodesTest {

    private MapOfNeighbours mapOfNodes;
    private static final String nodeID1 = "TestNode1";
    private static final String nodeID2 = "TestNode2";
    private static final String nodeID3 = "TestNode3";

    public MapOfNodesTest() {
    }

    @Before
    public void setUp() throws JSONException {
        this.mapOfNodes = new MapOfNeighbours();
        this.mapOfNodes.put(nodeID1, new NeighbourProperties(
                "{id:\"" + nodeID1 + "\", locationAbsolute:[1,2,3], locationRelative:[3,2,1] }"));
        this.mapOfNodes.put(nodeID2, new NeighbourProperties(
                "{id:\"" + nodeID2 + "\", locationRelative:[1,2,3] }"));
        this.mapOfNodes.put(nodeID3, new NeighbourProperties(
                "{id:\"" + nodeID3 + "\", distance:123 }"));
    }

    @After
    public void tearDown() {
        this.mapOfNodes = null;
    }

    /**
     * Test of toString method, of class MapOfNodes.
     *
     * @throws JSONException unable to parse toString result as a JSON
     * representation of the map
     */
    @Test
    public void testToString() throws JSONException {
        MapOfNeighbours newMapOfNodes = new MapOfNeighbours(this.mapOfNodes.toString());
        assertEquals(this.mapOfNodes, newMapOfNodes);
    }

    /**
     * Test of toJSONArray method, of class MapOfNodes.
     *
     * @throws JSONException fail to parse the map in JSON
     */
    @Test
    public void testToJSONArray() throws JSONException {
        MapOfNeighbours newMapOfNodes = new MapOfNeighbours(this.mapOfNodes.toJSONArray());
        assertEquals(this.mapOfNodes, newMapOfNodes);
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNodes.
     */
    @Test
    public void testGetNodesWithLocation_0args() {
        MapOfNeighbours result = this.mapOfNodes.getNodesWithLocation();
        assertEquals(1, result.size());
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNodes.
     */
    @Test
    public void testGetNodesWithLocation_Vector3D() {
        MapOfNeighbours result = this.mapOfNodes.getNodesWithLocation(
                new Vector3D(10, 10, 10));
        assertEquals(2, result.size());
        assertEquals(new Vector3D(11.0, 12.0, 13.0), result.get(nodeID2).getLocationAbsolute());
    }
}
