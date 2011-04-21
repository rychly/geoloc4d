package eu.esonia.but.geoloc4d.type;

import eu.esonia.but.geoloc4d.type.Vector3D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of class for vector coordinates in 3D (without algebraical operations).
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
        assertEquals(this.vector3D.toString(), "("
                + this.vector3D.getX() + ","
                + this.vector3D.getY() + ","
                + this.vector3D.getZ() + ")");
    }

    /**
     * Test of set method, of class Vector3D.
     */
    @Test
    public void testSet_String() {
        Vector3D newVector3D = new Vector3D();
        newVector3D.set("("
                + this.vector3D.getX() + ","
                + this.vector3D.getY() + ","
                + this.vector3D.getZ() + ")");
        assertEquals(this.vector3D.toString(), newVector3D.toString());
    }
}
