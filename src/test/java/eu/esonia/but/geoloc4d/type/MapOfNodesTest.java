package eu.esonia.but.geoloc4d.type;

import org.json.JSONArray;
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
    public void setUp() {
        this.mapOfNodes = new MapOfNeighbours();
        this.mapOfNodes.put(nodeID1, new NeighbourProperties(nodeID1
                + "{ locationAbsolute=(1,2,3); locationRelative=(3,2,1); }"));
        this.mapOfNodes.put(nodeID2, new NeighbourProperties(nodeID2
                + "{ locationRelative=(1,2,3); }"));
        this.mapOfNodes.put(nodeID3, new NeighbourProperties(nodeID3
                + "{ distance=123; }"));
    }

    @After
    public void tearDown() {
        this.mapOfNodes = null;
    }

    /**
     * Test of toString method, of class MapOfNeighbours.
     */
    @Test
    public void testToString() {
        assertEquals(this.mapOfNodes.get(nodeID1) + " + "
                + this.mapOfNodes.get(nodeID2) + " + "
                + this.mapOfNodes.get(nodeID3),
                this.mapOfNodes.toString());
    }

    /**
     * Test of set method, of class MapOfNeighbours.
     */
    @Test
    public void testSet_String() {
        MapOfNeighbours newMapOfNodes = new MapOfNeighbours();
        newMapOfNodes.set(this.mapOfNodes.get(nodeID1) + " + "
                + this.mapOfNodes.get(nodeID2) + " + "
                + this.mapOfNodes.get(nodeID3));
        assertEquals(this.mapOfNodes.toString(), newMapOfNodes.toString());
    }

    /**
     * Test of set method, of class MapOfNeighbours.
     *
     * @throws JSONException fail to parse a node's string representation in
     * JSON
     */
    @Test
    public void testSet_JSONArray() throws JSONException {
        MapOfNeighbours newMapOfNodes = new MapOfNeighbours();
        newMapOfNodes.set(new JSONArray(this.mapOfNodes.toJSONString()));
        assertEquals(this.mapOfNodes.toString(), newMapOfNodes.toString());
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNeighbours.
     */
    @Test
    public void testGetNodesWithLocation_0args() {
        MapOfNeighbours result = this.mapOfNodes.getNodesWithLocation();
        assertEquals(1, result.size());
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNeighbours.
     */
    @Test
    public void testGetNodesWithLocation_Vector3D() {
        MapOfNeighbours result = this.mapOfNodes.getNodesWithLocation(
                new Vector3D(10, 10, 10));
        assertEquals(2, result.size());
        assertEquals(new Vector3D(11.0, 12.0, 13.0), result.get(nodeID2).getLocationAbsolute());
    }
}
