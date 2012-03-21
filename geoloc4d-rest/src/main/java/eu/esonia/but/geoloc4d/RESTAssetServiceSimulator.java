package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.rest.NodeRestletApplication;
import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.Node;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONException;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Asset REST Service(s) Simulator.
 *
 * @author rychly
 */
public class RESTAssetServiceSimulator {

    public static void main(final String[] args) {
        // Check parameters
        if (args.length != 3) {
            System.err.println("Usage: java " + RESTAssetServiceSimulator.class.getName() + " <hostname> <port> <services.properties>");
            System.exit(-1);
        }

        // create a new component for RESTlets
        System.out.println("=== Creating a new component for RESTlets...");
        Component component = new Component();

        // add a new HTTP server listening on the specified port
        System.out.println("=== Adding a new HTTP server...");
        component.getServers().add(Protocol.HTTP, args[0], Integer.parseInt(args[1]));

        // Read services.properties for services, prepare and add the services to the device
        System.out.println("=== Loading nodes' definitions from file...");
        try {
            for (Node node : MapOfNodes.loadNodes(args[2],
                    "http://" + args[0] + ":" + args[1] + "/%/" + NodeRestletApplication.ROOTPATH).values()) {
                String path = node.getInfo().getURI().getPath();
                int firstSlash = path.indexOf('/', 1);
                path = path.substring(0, firstSlash > 0 ? firstSlash : path.length());
                System.out.println("=== a RESTlet will be created for node '"
                        + path + "':\n" + node.toString());
                // attach the RESTlet application for the node to the component
                component.getDefaultHost().attach(path, new NodeRestletApplication(node));
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

        // start the component
        System.out.println("=== Starting the component...");
        try {
            component.start();
        }
        catch (Exception ex) {
            System.err.println("!!! exception: " + ex.toString());
            System.exit(-5);
        }
    }
}