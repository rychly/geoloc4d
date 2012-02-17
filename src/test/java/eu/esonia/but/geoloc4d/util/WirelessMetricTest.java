package eu.esonia.but.geoloc4d.util;

import eu.esonia.but.geoloc4d.type.Vector3D;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of class for wireless metrics.
 *
 * @author rychly
 */
public class WirelessMetricTest {

    private Vector3D node0, nodeA, nodeB, nodeC, nodeD;
    private short rssiA, rssiB, rssiC, rssiD;
    private double rttA, rttB, rttC, rttD;
    private double signalStrengthAtMeter, propagationConstant, correctionFactor;

    public WirelessMetricTest() {
    }

    @Before
    public void setUp() {
        this.node0 = Vector3D.NULL;
        this.nodeA = new Vector3D(10, 00, 00);
        this.rssiA = -70;
        this.rttA = 0.025;
        this.nodeB = new Vector3D(00, 20, 00);
        this.rssiB = -79;
        this.rttB = 0.05;
        this.nodeC = new Vector3D(00, 00, 10);
        this.rssiC = -70;
        this.rttC = 0.025;
        this.nodeD = new Vector3D(20, 00, 00);
        this.rssiD = -79;
        this.rttD = 0.05;
        this.signalStrengthAtMeter = 40;
        this.propagationConstant = 3;
        this.correctionFactor = 400;
    }

    @After
    public void tearDown() {
        this.node0 = null;
        this.nodeA = null;
        this.nodeB = null;
        this.nodeC = null;
        this.nodeD = null;
    }

    /**
     * Test of compSignalStrengthAtMeter method, of class WirelessMetric.
     *
     * @throws WirelessMetricException assertion failed with the exception
     */
    @Test
    public void testCompSignalStrengthAtMeter() throws WirelessMetricException {
        assertEquals(this.signalStrengthAtMeter, WirelessMetric.compSignalStrengthAtMeter(
                this.rssiA, this.rssiB,
                this.nodeA.distance(this.node0), this.nodeB.distance(this.node0)),
                0.2);
    }

    /**
     * Test of compPropagationConstant method, of class WirelessMetric.
     *
     * @throws WirelessMetricException assertion failed with the exception
     */
    @Test
    public void testCompPropagationConstant() throws WirelessMetricException {
        assertEquals(this.propagationConstant, WirelessMetric.compPropagationConstant(
                this.rssiA, this.rssiB,
                this.nodeA.distance(this.node0), this.nodeB.distance(this.node0)),
                0.1);
    }

    /**
     * Test of compRssiFromDistance method, of class WirelessMetric.
     *
     * @throws WirelessMetricException assertion failed with the exception
     */
    @Test
    public void testCompRssiFromDistance() throws WirelessMetricException {
        assertEquals(this.rssiA, WirelessMetric.compRssiFromDistance(
                this.nodeA.distance(this.node0),
                this.signalStrengthAtMeter, this.propagationConstant));
    }

    /**
     * Test of compDistanceFromRssi method, of class WirelessMetric.
     *
     * @throws WirelessMetricException assertion failed with the exception
     */
    @Test
    public void testCompDistanceFromRssi() throws WirelessMetricException {
        assertEquals(this.nodeA.distance(this.node0), WirelessMetric.compDistanceFromRssi(
                this.rssiA,
                this.signalStrengthAtMeter, this.propagationConstant), 0.1);
    }

    /**
     * Test of compCorrectionFactorFromRttForDistance method, of class
     * WirelessMetric.
     *
     * @throws WirelessMetricException assertion failed with the exception
     */
    @Test
    public void testCompCorrectionFactorFromRttForDistance() throws WirelessMetricException {
        assertEquals(this.correctionFactor, WirelessMetric.compCorrectionFactorFromRttForDistance(
                this.rttA, this.nodeA.distance(this.node0)),
                0.1);
    }

    /**
     * Test of compDistanceFromRtt method, of class WirelessMetric.
     */
    @Test
    public void testCompDistanceFromRtt() {
        assertEquals(this.nodeA.distance(this.node0), WirelessMetric.compDistanceFromRtt(
                this.rttA, this.correctionFactor),
                0.1);
    }

    /**
     * Test of trilateration3D method, of class WirelessMetric.
     */
    @Test
    public void testTrilateration3D_6args() {
        Vector3D[] result = WirelessMetric.trilateration3D(
                this.nodeA, this.nodeA.distance(this.node0),
                this.nodeB, this.nodeB.distance(this.node0),
                this.nodeC, this.nodeC.distance(this.node0));
        assertEquals(0, Math.min(this.node0.distance(result[0]), this.node0.distance(result[1])), 0.001);
    }

    /**
     * Test of trilateration3D method, of class WirelessMetric.
     */
    @Test
    public void testTrilateration3D_8args() {
        assertEquals(0, this.node0.distance(
                WirelessMetric.trilateration3D(
                this.nodeA, this.nodeA.distance(this.node0),
                this.nodeB, this.nodeB.distance(this.node0),
                this.nodeC, this.nodeC.distance(this.node0),
                this.nodeD, this.nodeD.distance(this.node0))), 0.001);
    }
}
