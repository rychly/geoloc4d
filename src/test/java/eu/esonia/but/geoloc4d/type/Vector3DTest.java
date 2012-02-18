package eu.esonia.but.geoloc4d.type;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class for vector coordinates in 3D (without algebraical operations).
 *
 * @author rychly
 */
public class Vector3DTest {

    private Vector3D vector3D;

    public Vector3DTest() {
    }

    @Before
    public void setUp() {
        this.vector3D = new Vector3D(1.2, 3.4, 5.6);
    }

    @After
    public void tearDown() {
        this.vector3D = null;
    }

    /**
     * Test of equals method, of class Vector3D.
     */
    @Test
    public void testEquals() {
        assertTrue(this.vector3D.equals(new Vector3D(1.2, 3.4, 5.6)));
    }

    /**
     * Test of isUndefined method, of class Vector3D.
     */
    @Test
    public void testIsUndefined() {
        assertFalse(this.vector3D.isUndefined());
    }

    /**
     * Test of toString method, of class Vector3D.
     */
    @Test
    public void testToString() {
        JSONArray expected = new JSONArray();
        expected.put(this.vector3D.getX());
        expected.put(this.vector3D.getY());
        expected.put(this.vector3D.getZ());
        assertEquals(expected.toString(),
                this.vector3D.toString());
    }

    /**
     * Test of toJSONArray method, of class Vector3D.
     * @throws JSONException fail to parse the vector in JSON
     */
    @Test
    public void testToJSONArray() throws JSONException {
        Vector3D newVector3D = new Vector3D(this.vector3D.toJSONArray());
        assertEquals(this.vector3D, newVector3D);
    }
}
