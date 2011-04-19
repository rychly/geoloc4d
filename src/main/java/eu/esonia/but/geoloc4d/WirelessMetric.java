package eu.esonia.but.geoloc4d;

import java.util.Arrays;

/**
 * Various functions for measurements in wireless networks.
 * RSSI related computations are based on the formula provided by Texas Instruments, which represented the relationship between RSSI and the estimated 1-D distance.
 * IEEE 802's RSSI are unitless and in the range 0 to 255, expressible as a one-byte unsigned integer.
 * Trilateration is based on Wikipedia.
 * @author rychly
 */
public class WirelessMetric {

    /**
     * Compute a received signal strength at 1 meter distance according to a reference node and its RSSIs and actual distances to nodes A and B.
     * @param rssiToA RSSI value of node A in the reference node
     * @param rssiToB RSSI value of node B in the reference node
     * @param distanceToA distance of node A from the reference node
     * @param distanceToB distance of node B from the reference node
     * @return the received signal strength at 1 meter distance
     */
    public static double compSignalStrengthAtMeter(short rssiToA, short rssiToB, double distanceToA, double distanceToB) {
        return (rssiToA * Math.log10(distanceToB) - rssiToB * Math.log10(distanceToA))
                / (Math.log10(distanceToA) - Math.log10(distanceToB));
    }

    /**
     * Compute a propagation constant according to a reference node and its RSSIs and actual distances to nodes A and B.
     * @param rssiToA RSSI value of node A in the reference node
     * @param rssiToB RSSI value of node B in the reference node
     * @param distanceToA distance of node A from the reference node
     * @param distanceToB distance of node B from the reference node
     * @return the propagation constant
     */
    public static double compPropagationConstant(short rssiToA, short rssiToB, double distanceToA, double distanceToB) {
        return (rssiToA - rssiToB)
                / (10 * (Math.log10(distanceToB) - Math.log10(distanceToA)));
    }

    /**
     * Compute a received signal RSSI value from distance of a reference node and a blind node, the received signal strength at 1 meter distance, and propagation constant.
     * @param distance distance of a reference node and a blind node
     * @param signalStrengthAtMeter received signal strength at 1 meter distance
     * @param propagationConstant propagation constant
     * @return received signal RSSI value
     */
    public static short compRssiFromDistance(double distance, double signalStrengthAtMeter, double propagationConstant) {
        return (short) Math.round(-1 * (10 * propagationConstant * Math.log10(distance) + signalStrengthAtMeter));
    }

    /**
     * Compute a distance of a reference node and a blind node from received signal RSSI value and strength at 1 meter distance and propagation constant.
     * @param rssi received signal RSSI value
     * @param signalStrengthAtMeter received signal strength at 1 meter distance
     * @param propagationConstant propagation constant
     * @return the distance
     */
    public static double compDistanceFromRssi(short rssi, double signalStrengthAtMeter, double propagationConstant) {
        return Math.pow(10, (rssi + signalStrengthAtMeter)
                / (-10 * propagationConstant));
    }

    /**
     * Performs (undeterministic) trilateration in 3D according to locations of nodes A, B, C, and their distances from a blind node.
     * @param nodeA location of node A
     * @param distanceA distance of node A and the blind node
     * @param nodeB location of node B
     * @param distanceB distance of node B and the blind node
     * @param nodeC location of node C
     * @param distanceC distance of node C and the blind node
     * @return location of the blind node
     */
    public static Vector3D[] trilateration3D(final Vector3D nodeA, double distanceA,
            final Vector3D nodeB, double distanceB,
            final Vector3D nodeC, double distanceC) {
        // step 1: we have Cartesian coordinates, so no transformation from lat/long coordinates is needed
        // step 2: translating the points to nodeA be at the origin
        Vector3D tmpB = nodeB.sub(nodeA);
        Vector3D tmpC = nodeC.sub(nodeA);
        // step 3: rotating to tmpB be on the X axis and tmpC be in the X-Y plane
        double d = tmpB.norm();
        Vector3D ex = tmpB.scale(1 / d);
        double i = ex.dot(tmpC);
        Vector3D ey = tmpC.sub(ex.scale(i));
        ey = ey.scale(1 / ey.norm());
        double j = ey.dot(tmpC);
        Vector3D ez = ex.cross(ey);
        // step 4: derivation from distances
        double x = (Math.pow(distanceA, 2) - Math.pow(distanceB, 2) + Math.pow(d, 2)) / (2 * d);
        double y = ((Math.pow(distanceA, 2) - Math.pow(distanceC, 2) + Math.pow(i, 2) + Math.pow(j, 2)) / (2 * j)) - ((i * x) / j);
        double zPlus = Math.sqrt(Math.pow(distanceA, 2) - Math.pow(x, 2) - Math.pow(y, 2));
        // step 5: translating the points in the original coordinate system
        return new Vector3D[]{
                    nodeA.add(ex.scale(x)).add(ey.scale(y)).add(ez.scale(zPlus)),
                    nodeA.add(ex.scale(x)).add(ey.scale(y)).add(ez.scale(-1 * zPlus))};
        // step 6: we have Cartesian coordinates, so no transformation to lat/long coordinates is needed
    }

    /**
     * Performs (deterministic) trilateration in 3D according to locations of nodes A, B, C, D, and their distances from a blind node.
     * @param nodeA location of node A
     * @param distanceA distance of node A and the blind node
     * @param nodeB location of node B
     * @param distanceB distance of node B and the blind node
     * @param nodeC location of node C
     * @param distanceC distance of node C and the blind node
     * @param nodeD location of node D
     * @param distanceD distance of node D and the blind node
     * @return location of the blind node
     */
    public static Vector3D trilateration3D(final Vector3D nodeA, double distanceA,
            final Vector3D nodeB, double distanceB,
            final Vector3D nodeC, double distanceC,
            final Vector3D nodeD, double distanceD) {
        // perform trilateration on two triplets of nodes
        Vector3D[] trilateratedABC = WirelessMetric.trilateration3D(
                nodeA, distanceA, nodeB, distanceB, nodeC, distanceC);
        Vector3D[] trilateratedABD = WirelessMetric.trilateration3D(
                nodeA, distanceA, nodeB, distanceB, nodeD, distanceD);
        // merge results and select averange of two most similar
        Vector3D[] result = Arrays.copyOf(trilateratedABC, trilateratedABC.length + trilateratedABD.length);
        System.arraycopy(trilateratedABD, 0, result, trilateratedABC.length, trilateratedABD.length);
        result = Vector3D.selectTwoMostSimilar(result);
        return result[0].avg(result[1]);
    }
}
