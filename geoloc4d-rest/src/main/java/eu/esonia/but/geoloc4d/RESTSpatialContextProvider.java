package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.rest.NodeRestletCrawler;
import eu.esonia.but.geoloc4d.rest.NodeRestletInterface;
import eu.esonia.but.geoloc4d.rest.NodeRestletProxy;
import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import eu.esonia.but.geoloc4d.util.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ResourceException;

/**
 * Spatial context provider (RESTful client).
 *
 * @author rychly
 */
public class RESTSpatialContextProvider {

    public static void main(final String[] args) throws InterruptedException, JSONException {
        // check parameters
        if (args.length < 2) {
            System.err.println("Usage: java " + RESTSpatialContextProvider.class.getName() + " [rssi-startegy|rtt-strategy] <URIs>");
            System.exit(-1);
        }

        // instantiate the client connector, and configure it
        System.out.println("=== Starting client connector...");
        Client client = new Client(new Context(), Protocol.HTTP);
        client.getContext().getParameters().add("useForwardedForHeader", "false");

        // crawl throught the RESTlets given by defined URIs
        System.out.println("=== Starting RESTlets crawler...");
        NodeRestletCrawler nodeRestletCrawler =
                new NodeRestletCrawler(client, Arrays.copyOfRange(args, 1, args.length));
        nodeRestletCrawler.crawlGatheredNodes();

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
                MapOfNodes mapOfNodes = nodeRestletCrawler.getMapOfNodesForGatheredRestlets(true);
                System.out.println("=== the calibration will be performed for nodes:\n"
                        + mapOfNodes.toString());
                trilaterationStrategy.calibrateMetric(mapOfNodes);
            }
            catch (IOException | ResourceException | JSONException ex) {
                // RESTlet error will be ignored -- next time, the network may be ready
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
        Set<NodeRestletProxy> restlets;
        boolean completelyLocalised;
        do {
            System.out.println("=== Performing trilateration of detected nodes...");
            // nodes can be completely localised only if there are some
            //completelyLocalised = !((restlets == null) || restlets.isEmpty());
            completelyLocalised = false; // infinite loop for debuging
            // get actually gathered the nodes' RESTlets
            restlets = nodeRestletCrawler.getGatheredRestlets();
            for (NodeRestletInterface restlet : restlets) {
                System.out.println("=== found a node's RESTlet: " + restlet.toString());
                try {
                    // get data of node and check if it is localised
                    NodeData nodeData = new NodeData(new JSONObject(restlet.getInfo()));
                    System.out.println("=== the RESTlet represents node with ID: " + nodeData.getID());
                    if (nodeData.isAbsolutelyLocalised()) {
                        // if it is localised, print its location
                        System.out.println("=== the node is localised as: " + nodeData.getLocationAbsolute().toString());
                    } else {
                        // if not, get scan of its neighbours and perform trilateration
                        System.out.println("=== the node is not localised, locating its neighbours...");
                        MapOfNeighbours nodeNeighbours = new MapOfNeighbours(new JSONArray(restlet.getScan()));
                        nodeNeighbours.setLocationsFromNodes(nodeRestletCrawler.getMapOfNodesForGatheredRestlets(false));
                        System.out.println("=== preparing for the node's trilateration with neighbours:\n" + nodeNeighbours.toString());
                        MapOfNeighbours selectedNeighbours =
                                trilaterationStrategy.prepareNodesForTrilateration(nodeData, nodeNeighbours);
                        System.out.println("=== the trilateration will be performed with nodes:\n" + selectedNeighbours.toString());
                        Vector3D newLocation =
                                trilaterationStrategy.doTrilateration(selectedNeighbours);
                        System.out.println("=== the node's location will be set to the trilateration's result: " + newLocation.toString());
                        // finally, set trilateration result as the node's location
                        restlet.postLocationAbsolute(newLocation.toJSONString());
                    }
                }
                catch (IOException | ResourceException | JSONException ex) {
                    // RESTlet error will be ignored -- we will try it next time
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
                    + nodeRestletCrawler.getMapOfNodesForGatheredRestlets(false).toString());
        }
        catch (IOException ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-2);
        }
        catch (JSONException ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-3);
        }
    }
}