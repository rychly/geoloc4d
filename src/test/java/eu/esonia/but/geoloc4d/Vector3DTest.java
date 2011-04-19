package eu.esonia.but.geoloc4d;

import junit.framework.TestCase;

/**
 * Test of class for vector coordinates in 3D (without algebraical operations).
 * @author rychly
 */
public class Vector3DTest extends TestCase {

    private Vector3D vector3D;

    public Vector3DTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        this.vector3D = new Vector3D(1.2, 3.4, 5.6);
    }

    @Override
    protected void tearDown() throws Exception {
        this.vector3D = null;
    }

    /**
     * Test of isUndefined method, of class Vector3D.
     */
    public void testIsUndefined() {
        assertEquals(this.vector3D.isUndefined(), false);
    }

    /**
     * Test of toString method, of class Vector3D.
     */
    public void testToString() {
        assertEquals(this.vector3D.toString(), "("
                + this.vector3D.getX() + ","
                + this.vector3D.getY() + ","
                + this.vector3D.getZ() + ")");
    }

    /**
     * Test of set method, of class Vector3D.
     */
    public void testSet_String() {
        Vector3D newVector3D = new Vector3D();
        newVector3D.set("("
                + this.vector3D.getX() + ","
                + this.vector3D.getY() + ","
                + this.vector3D.getZ() + ")");
        assertEquals(this.vector3D.toString(), newVector3D.toString());
    }
}
