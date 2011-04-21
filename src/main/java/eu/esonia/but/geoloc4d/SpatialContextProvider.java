package eu.esonia.but.geoloc4d;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.Vector3D;
import org.ws4d.java.DPWSFramework;
import org.ws4d.java.client.SearchParameter;
import org.ws4d.java.communication.HTTPBinding;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.DefaultDevice;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.Service;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.types.QName;
import org.ws4d.java.types.QNameSet;
import org.ws4d.java.util.Log;
import org.ws4d.java.util.ParameterUtil;

/**
 * Spatial context provider (DPWS client).
 * @author rychly
 */
public class SpatialContextProvider {

    public static void provideSpatialContext(Service service) throws InvocationException, TimeoutException {
        Operation operation;
        ParameterValue input, result;

        // We need to get the operation AssetService.ScanNeighbours from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        operation = service.getAnyOperation(
                new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE),
                AssetService.ScanNeighbours.class.getSimpleName());
        // now lets invoke the operation
        result = operation.invoke(null);

        // Trilateration algorithm.
        MapOfNodes scanList = new MapOfNodes(ParameterUtil.getString(result, null));
        // do trilateration...
        // TODO
        Vector3D location = new Vector3D(10, 20, 30);

        // We need to get the operation AssetService.SetLocation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        operation = service.getAnyOperation(
                new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE),
                AssetService.SetLocation.class.getSimpleName());
        input = operation.createInputValue();
        ParameterUtil.setString(input, null, location.toString());
        // now lets invoke the operation
        result = operation.invoke(input);
    }

    public static Vector3D getLocation(Service service) throws InvocationException, TimeoutException {
        // We need to get the operation AssetService.GetLocation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = service.getAnyOperation(
                new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE),
                AssetService.GetLocation.class.getSimpleName());
        // now lets invoke the operation
        ParameterValue result = operation.invoke(null);
        // location
        return new Vector3D(ParameterUtil.getString(result, null));
    }

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

        // Create DPWS client
        SpatialContextProviderClient client = new SpatialContextProviderClient();

        // Define a service to search and trigger the search
        QName serviceType = new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE);
        SearchParameter params = new SearchParameter();
        params.setServiceTypes(new QNameSet(serviceType));
        client.clear();
        client.searchService(params);

        while (true) {
            // Wait (in msec)
            System.err.println("Waiting...");
            Thread.sleep(3000);
            // Explore available services and provide them with spatial context.
            for (Service service : client.getAvailableServices()) {
                try {
                    System.err.println("Provide spatial context for service: " + service.toString());
                    SpatialContextProvider.provideSpatialContext(service);
                } catch (InvocationException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

            // Print location of available services.
            for (Service service : client.getAvailableServices()) {
                try {
                    System.err.println("Get location of service: " + service.toString());
                    System.out.println(service.getServiceId().toString() + " = "
                            + SpatialContextProvider.getLocation(service));
                } catch (InvocationException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
