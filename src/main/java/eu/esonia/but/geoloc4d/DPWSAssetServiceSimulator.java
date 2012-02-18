package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.dpws.NodeService;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.Node;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONException;
import org.ws4d.java.DPWSFramework;
import org.ws4d.java.communication.HTTPBinding;
import org.ws4d.java.service.DefaultDevice;
import org.ws4d.java.service.LocalService;
import org.ws4d.java.types.LocalizedString;
import org.ws4d.java.util.Log;

/**
 * Asset Service(s) Simulator.
 *
 * @author rychly
 */
public class DPWSAssetServiceSimulator {

    public static void main(final String[] args) {
        // Check parameters
        // To enable multicast on loopback interface do "ifconfig lo multicast"
        if (args.length != 4) {
            System.err.println("Usage: java " + DPWSAssetServiceSimulator.class.getName() + " <hostname> <port> <path/> <services.properties>");
            System.exit(-1);
        }

        // Start DPWSFramework without any properties file (e.g. bindings and metadata for device and services)
        System.out.println("=== Starting DPWS framework...");
        Log.setLogLevel(Log.DEBUG_LEVEL_ERROR);
        DPWSFramework.start(new String[]{});

        // Set up a device using the first command-line parameter for its binding (the device can be accessed over)
        System.out.println("=== Setting up a DPWS device...");
        DefaultDevice device = new DefaultDevice();
        //device.addBinding(new HTTPBinding(new URI(args[0] + "/" + DPWSAssetServiceSimulator.class.getSimpleName()))); // BUG
        device.addBinding(new HTTPBinding(args[0], Integer.parseInt(args[1]), args[2] + DPWSAssetServiceSimulator.class.getSimpleName()));
        device.addFriendlyName(LocalizedString.LANGUAGE_EN, DPWSAssetServiceSimulator.class.getSimpleName());
        device.setManufacturerUrl(NodeService.NAMESPACE);

        // Read services.properties for services, prepare and add the services to the device
        System.out.println("=== Loading nodes' definitions from file...");
        try {
            for (Node node : MapOfNodes.loadNodes(args[3]).values()) {
                System.out.println("=== a service will be created for node:\n" + node.toString());
                // Create service
                LocalService service = new NodeService(node);
                // Set binding for the service, i.e. the address it will accept
                //service.addBinding(new HTTPBinding(new URI(args[0] + "/" + NodeService.class.getSimpleName() + "/" + serviceDescription.name))); // BUG
                service.addBinding(new HTTPBinding(args[0],
                        Integer.parseInt(args[1]),
                        args[2] + NodeService.class.getSimpleName() + "/" + node.getSelf().getID()));
                System.out.println("=== the created service will be added to the device as: "
                        + service.toString());
                // Add the service to the device
                device.addService(service);
            }
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("!!! exception: The file " + args[1] + " with service descriptions was not found!\n" + fnfe.toString());
            System.exit(-2);
        }
        catch (IOException ioe) {
            System.err.println("!!! exception: Problem loading file " + args[0] + " with service descriptions!\n" + ioe.toString());
            System.exit(-3);
        }
        catch (JSONException je) {
            System.err.println("!!! exception: Cannot format JSON string!\n" + je.toString());
            System.exit(-4);
        }

        // Start the device
        System.out.println("=== Starting the device...");
        try {
            device.start();
        }
        catch (IOException ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-4);
        }
    }
}
