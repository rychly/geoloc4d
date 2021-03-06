package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.dpws.NodeServiceDetector;
import eu.esonia.but.geoloc4d.dpws.NodeServiceDetectorHeartbeat;
import eu.esonia.but.geoloc4d.dpws.NodeServiceInterface;
import eu.esonia.but.geoloc4d.dpws.NodeServiceProxy;
import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import eu.esonia.but.geoloc4d.util.*;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ws4d.java.DPWSFramework;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.util.Log;

/**
 * Spatial context provider (DPWS client).
 *
 * @author rychly
 */
public class DPWSSpatialContextProvider {

    public static void main(final String[] args) throws InterruptedException, JSONException {
        // Check parameters
        // To enable multicast on loopback interface do "ifconfig lo multicast"
        if (args.length != 1) {
            System.err.println("Usage: java " + DPWSSpatialContextProvider.class.getName() + " [rssi-startegy|rtt-strategy]");
            System.exit(-1);
        }

        // Start DPWSFramework without any properties file (e.g. bindings and metadata for device and services)
        System.out.println("=== Starting DPWS framework...");
        Log.setLogLevel(Log.DEBUG_LEVEL_ERROR);
        DPWSFramework.start(new String[]{});

        // Create DPWS client, its heartbeat and periodically search for available services
        System.out.println("=== Starting service detector...");
        NodeServiceDetector nodeServiceDetector = new NodeServiceDetector();
        NodeServiceDetectorHeartbeat nodeServiceDetectorHeartbeat =
                new NodeServiceDetectorHeartbeat(nodeServiceDetector);
        nodeServiceDetectorHeartbeat.schedule(10000);

        // Create a trilateration strategy
        System.out.println("=== Selecting a trilateration strategy...");
        TrilaterationStrategy trilaterationStrategy = null;
        if (args[0].equalsIgnoreCase("rssi-strategy")) {
            trilaterationStrategy = TrilaterationStrategyFactory.newStrategyWithRSSI();
        } else if (args[0].equalsIgnoreCase("rtt-strategy")) {
            trilaterationStrategy = TrilaterationStrategyFactory.newStrategyWithRTT();
        } else {
            System.err.println("!!! Unknown trilateration strategy '" + args[0] + "'!");
            System.exit(-2);
        }

        // Calibrate the strategy's metric
        while (!trilaterationStrategy.isCalibrated()) {
            System.out.println("=== Calibrate the trilateration strategy's metric...");
            Thread.sleep(1000);
            try {
                MapOfNodes mapOfNodes = nodeServiceDetector.getMapOfNodesForDetectedServices(true);
                System.out.println("=== the calibration will be performed for nodes:\n"
                        + mapOfNodes.toString());
                trilaterationStrategy.calibrateMetric(mapOfNodes);
            }
            catch (InvocationException ex) {
                // service error will be ignored -- next time, the network may be ready
                System.err.println("!!! exception: " + ex.toString());
            }
            catch (TimeoutException ex) {
                // service error will be ignored -- next time, the network may be ready
                System.err.println("!!! exception: " + ex.toString());
            }
            catch (TrilaterationStrategyException ex) {
                // calibration error will be ignored -- next time, there may be enought nodes
                System.err.println("!!! exception: " + ex.toString());
            }
        }
        System.out.println("=== The trilateration strategy has calibrated metric:");
        if (trilaterationStrategy instanceof StrategyWithRSSI) {
            System.out.println(
                    "=== received signal strength at 1 meter distance = "
                    + ( (StrategyWithRSSI) trilaterationStrategy ).getSignalStrengthAtMeter()
                    + "\n=== propagation constant = "
                    + ( (StrategyWithRSSI) trilaterationStrategy ).getPropagationConstant());
        } else if (trilaterationStrategy instanceof StrategyWithRTT) {
            System.out.println(
                    "=== correction factor for conversion of RTT into actual distance = "
                    + ( (StrategyWithRTT) trilaterationStrategy ).getCorrectionFactor());
        } else {
            System.out.println(
                    "=== an unknown implementation of the strategy!");
        }

        // Localise all detected nodes
        List<NodeServiceProxy> services;
        boolean completelyLocalised;
        do {
            System.out.println("=== Performing trilateration of detected nodes...");
            // nodes can be completely localised only if there are some
            //completelyLocalised = !((services == null) || services.isEmpty());
            completelyLocalised = false; // infinite loop for debuging
            // get actually detected the nodes' services
            services = nodeServiceDetector.getDetectedServices();
            for (NodeServiceInterface service : services) {
                System.out.println("=== found a node's service: " + service.toString());
                try {
                    // get data of node and check if it is localised
                    NodeData nodeData = new NodeData(new JSONObject(service.getInfo()));
                    System.out.println("=== the service represents node: " + nodeData.getID());
                    if (nodeData.isAbsolutelyLocalised()) {
                        // if it is localised, print its location
                        System.out.println("=== the node is localised as: " + nodeData.getLocationAbsolute().toString());
                    } else {
                        // if not, get scan of its neighbours and perform trilateration
                        System.out.println("=== the node is not localised, locating its neighbours...");
                        MapOfNeighbours nodeNeighbours = new MapOfNeighbours(new JSONArray(service.getScan()));
                        nodeNeighbours.setLocationsFromNodes(nodeServiceDetector.getMapOfNodesForDetectedServices(false));
                        System.out.println("=== preparing for the node's trilateration with neighbours:\n" + nodeNeighbours.toString());
                        MapOfNeighbours selectedNeighbours =
                                trilaterationStrategy.prepareNodesForTrilateration(nodeData, nodeNeighbours);
                        System.out.println("=== the trilateration will be performed with nodes:\n" + selectedNeighbours.toString());
                        Vector3D newLocation =
                                trilaterationStrategy.doTrilateration(selectedNeighbours);
                        System.out.println("=== the node's location will be set to the trilateration's result: " + newLocation.toString());
                        // finally, set trilateration result as the node's location
                        service.setLocationAbsolute(newLocation.toJSONString());
                    }
                }
                catch (InvocationException ex) {
                    // service error will be ignored -- we will try it next time
                    System.err.println("!!! exception: " + ex.toString());
                    completelyLocalised = false;
                }
                catch (TimeoutException ex) {
                    // service error will be ignored -- we will try it next time
                    System.err.println("!!! exception: " + ex.toString());
                    completelyLocalised = false;
                }
                catch (TrilaterationStrategyException ex) {
                    // trilateration error will be ignored -- we will try it next time
                    System.err.println("!!! exception: " + ex.toString());
                    completelyLocalised = false;
                }
            }
            Thread.sleep(3000);
        } while (!completelyLocalised);
        try {
            // Print all nodes including their locations
            System.out.println("=== Localised nodes (without scan of their neighbours):\n"
                    + nodeServiceDetector.getMapOfNodesForDetectedServices(false).toString());
        }
        catch (InvocationException ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-2);
        }
        catch (TimeoutException ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-3);
        }
    }
}
