package eu.esonia.but.geoloc4d;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.ws4d.java.DPWSFramework;
import org.ws4d.java.communication.HTTPBinding;
import org.ws4d.java.service.DefaultDevice;
import org.ws4d.java.service.LocalService;
import org.ws4d.java.types.LocalizedString;
import org.ws4d.java.util.Log;

/**
 * Asset Service(s) Simulator.
 * @author rychly
 */
public class AssetServiceSimulator {

    private static Map<String, ServiceDescription> readServicesDescription(String filename) throws FileNotFoundException, IOException {
        Map<String, ServiceDescription> result = new HashMap<String, ServiceDescription>();
        // open file and read properties
        Properties servicesProperties = new Properties();
        FileInputStream servicesIniFile = new FileInputStream(filename);
        try {
            servicesProperties.load(servicesIniFile);
        } finally {
            servicesIniFile.close();
        }
        // go throught the properties and add services' descriptions into the map
        for (Map.Entry<Object, Object> pair : servicesProperties.entrySet()) {
            ServiceDescription serviceDescription;
            String[] serviceNameTokens = pair.getKey().toString().split("\\.", 2);
            // find a related service description if exists, create new otherwise
            if (result.containsKey(serviceNameTokens[0])) {
                serviceDescription = result.get(serviceNameTokens[0]);
            } else {
                serviceDescription = new ServiceDescription();
            }
            // fill the service description
            serviceDescription.name = serviceNameTokens[0];
            if (serviceNameTokens[1].equalsIgnoreCase("location")) {
                serviceDescription.location = new Vector3D(pair.getValue().toString());
            } else if (serviceNameTokens[1].equalsIgnoreCase("scan")) {
                serviceDescription.scanList = new ScanList(pair.getValue().toString());
            }
            // put the service description into the map
            result.put(serviceDescription.name, serviceDescription);
        }
        return result;
    }

    public static void main(String[] args) {
        /*
        {
        final String directory = (new File(AssetServiceSimulator.class.getResource(
        AssetServiceSimulator.class.getSimpleName() + ".class").
        toString().substring(5))).getParent();
        args = new String[]{
        // To enable multicast on loopback interface do "ifconfig lo multicast"
        "localhost", "4321", "/testing/",
        directory + "/" + AssetService.class.getSimpleName() + "-testing.properties"
        };
        }
        //*/

        // Check parameters
        if (args.length != 4) {
            System.err.println("Usage: java " + AssetServiceSimulator.class.getName() + " <hostname> <port> <path/> <services.properties>");
            System.exit(-1);
        }

        // We don't need debug-output. So we disable it.
        Log.setLogLevel(Log.DEBUG_LEVEL_DEBUG);

        // Start DPWSFramework without any properties file (e.g. bindings and metadata for device and services)
        DPWSFramework.start(new String[]{});

        // Set up a device using the first command-line parameter for its binding (the device can be accessed over)
        DefaultDevice device = new DefaultDevice();
        //device.addBinding(new HTTPBinding(new URI(args[0] + "/" + AssetServiceSimulator.class.getSimpleName()))); // BUG
        device.addBinding(new HTTPBinding(args[0], Integer.parseInt(args[1]), args[2] + AssetServiceSimulator.class.getSimpleName()));
        device.addFriendlyName(LocalizedString.LANGUAGE_EN, AssetServiceSimulator.class.getSimpleName());
        device.addModelName(LocalizedString.LANGUAGE_EN, "Model v0.1");
        device.addManufacturer(LocalizedString.LANGUAGE_EN, "Marek Rychly");
        device.setManufacturerUrl("http://www.fit.vutbr.cz/~rychly");

        // Read services.properties for services, prepare and add the services to the device
        try {
            for (ServiceDescription serviceDescription : AssetServiceSimulator.readServicesDescription(args[3]).values()) {
                // Create service
                LocalService service = new AssetService(-1);
                // Set binding for the service, i.e. the address it will accept
                //service.addBinding(new HTTPBinding(new URI(args[0] + "/" + AssetService.class.getSimpleName() + "/" + serviceDescription.name))); // BUG
                service.addBinding(new HTTPBinding(args[0], Integer.parseInt(args[1]), args[2] + AssetService.class.getSimpleName() + "/" + serviceDescription.name));
                // Add the service to the device
                device.addService(service);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("The file " + args[1] + " with service descriptions was not found!\n" + fnfe.toString());
            System.exit(-2);
        } catch (IOException ioe) {
            System.err.println("Problem loading file " + args[0] + " with service descriptions!\n" + ioe.toString());
            System.exit(-3);
        }
        try {
            device.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServiceDescription {

    String name;
    Vector3D location;
    ScanList scanList;

    ServiceDescription() {
        this.name = "Unknown";
        this.location = new Vector3D();
        this.scanList = new ScanList();
    }
}
