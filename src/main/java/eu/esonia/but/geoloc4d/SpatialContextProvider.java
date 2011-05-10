package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.service.NodeServiceDetector;
import eu.esonia.but.geoloc4d.service.NodeServiceDetectorHeartbeat;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ws4d.java.DPWSFramework;
import org.ws4d.java.communication.HTTPBinding;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.DefaultDevice;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.util.Log;

/**
 * Spatial context provider (DPWS client).
 * @author rychly
 */
public class SpatialContextProvider {

    public static void main(String[] args) throws InterruptedException {
        /*
        {
        args = new String[]{
        // To enable multicast on loopback interface do "ifconfig lo multicast"
        "localhost", "4421", "/testing/"
        };
        }
        //*/

        // Check parameters
        if (args.length != 3) {
            System.err.println("Usage: java " + SpatialContextProvider.class.getName() + " SpatialContextProvider <hostname> <port> <path/>");
            System.exit(-1);
        }

        // We don't need debug-output. So we disable it.
        Log.setLogLevel(Log.DEBUG_LEVEL_DEBUG);

        // Start DPWSFramework without any properties file (e.g. bindings and metadata for device and services)
        DPWSFramework.start(new String[]{});

        // Set up a device using the first command-line parameter for its binding (the device can be accessed over)
        DefaultDevice device = new DefaultDevice();
        //device.addBinding(new HTTPBinding(new URI(args[0] + "/" + SpatialContextProvider.class.getSimpleName()))); // BUG
        device.addBinding(new HTTPBinding(args[0], Integer.parseInt(args[1]), args[2] + SpatialContextProvider.class.getSimpleName()));
        device.addFriendlyName(null, SpatialContextProvider.class.getSimpleName());
        device.addModelName(null, "Model v0.1");
        device.addManufacturer(null, "Marek Rychly");
        device.setManufacturerUrl("http://www.fit.vutbr.cz/~rychly");

        // Create DPWS client, its heartbeat and periodically search for available services
        NodeServiceDetector nodeServiceDetector = new NodeServiceDetector();
        NodeServiceDetectorHeartbeat nodeServiceDetectorHeartbeat =
                new NodeServiceDetectorHeartbeat(nodeServiceDetector);
        nodeServiceDetectorHeartbeat.schedule(10000);

        // Periodically get map of nodes of detected services and perform trilateration for blind nodes
        while (true) {
            try {
                MapOfNodes mapOfNodes = nodeServiceDetector.getMapOfNodesForDetectedServices();
                System.err.println("=====\n" + mapOfNodes.toString() + "=====");
            } catch (InvocationException ex) {
                Logger.getLogger(SpatialContextProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(SpatialContextProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Wait (in msec)
            System.err.println("Waiting...");
            Thread.sleep(3000);
        }
    }
}
