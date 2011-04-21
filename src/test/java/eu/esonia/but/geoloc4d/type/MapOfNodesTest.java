package eu.esonia.but.geoloc4d.type;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeProperties;
import eu.esonia.but.geoloc4d.type.Vector3D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of class for map of nodes as a hash-map.
 * @author rychly
 */
public class MapOfNodesTest {

    private MapOfNodes mapOfNodes;
    private static final String nodeID1 = "TestNode1";
    private static final String nodeID2 = "TestNode2";
    private static final String nodeID3 = "TestNode3";

    public MapOfNodesTest() {
    }

    @Before
    public void setUp() {
        this.mapOfNodes = new MapOfNodes();
        this.mapOfNodes.put(nodeID1, new NodeProperties(nodeID1
                + "{ locationAbsolute=(1,2,3); locationRelative=(3,2,1); }"));
        this.mapOfNodes.put(nodeID2, new NodeProperties(nodeID2
                + "{ locationRelative=(1,2,3); }"));
        this.mapOfNodes.put(nodeID3, new NodeProperties(nodeID3
                + "{ distance=123; }"));
    }

    @After
    public void tearDown() {
        this.mapOfNodes = null;
    }

    /**
     * Test of toString method, of class MapOfNodes.
     */
    @Test
    public void testToString() {
        assertEquals(this.mapOfNodes.get(nodeID1) + " + "
                + this.mapOfNodes.get(nodeID2) + " + "
                + this.mapOfNodes.get(nodeID3),
                this.mapOfNodes.toString());
    }

    /**
     * Test of set method, of class MapOfNodes.
     */
    @Test
    public void testSet_String() {
        MapOfNodes newMapOfNodes = new MapOfNodes();
        newMapOfNodes.set(this.mapOfNodes.get(nodeID1) + " + "
                + this.mapOfNodes.get(nodeID2) + " + "
                + this.mapOfNodes.get(nodeID3));
        assertEquals(this.mapOfNodes.toString(), newMapOfNodes.toString());
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNodes.
     */
    @Test
    public void testGetNodesWithLocation_0args() {
        MapOfNodes result = this.mapOfNodes.getNodesWithLocation();
        assertEquals(1, result.size());
    }

    /**
     * Test of getNodesWithLocation method, of class MapOfNodes.
     */
    @Test
    public void testGetNodesWithLocation_Vector3D() {
        MapOfNodes result = this.mapOfNodes.getNodesWithLocation(
                new Vector3D(10, 10, 10));
        assertEquals(2, result.size());
        assertEquals(new Vector3D(11.0, 12.0, 13.0), result.get(nodeID2).locationAbsolute);
    }
}
