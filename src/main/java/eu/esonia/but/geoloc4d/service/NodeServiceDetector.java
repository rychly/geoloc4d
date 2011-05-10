package eu.esonia.but.geoloc4d.service;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ws4d.java.client.DefaultClient;
import org.ws4d.java.client.SearchParameter;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.reference.ServiceReference;
import org.ws4d.java.types.URI;

/**
 * DPWS client to detect available NodeService services and create their proxies.
 * @author rychly
 */
public class NodeServiceDetector extends DefaultClient {

    /**
     * The actual list of detected services.
     */
    private List<NodeServiceProxy> detectedServices;
    /**
     * The actual number of time-outs.
     */
    private int numberOfTimeOuts;

    /**
     * Nonparametric constructor.
     */
    public NodeServiceDetector() {
        this.detectedServices = Collections.synchronizedList(
                new ArrayList<NodeServiceProxy>());
        this.numberOfTimeOuts = 0;
    }

    /**
     * This method is called each time a service matching the initial search criteria (as contained within argument search) has been found. 
     * It is called if there are search-results, after method searchService(new SearchParameter()) is triggered.
     * @param serviceRef a reference to the matching service
     * @param search the list of criteria the search was initiated with
     */
    @Override
    public void serviceFound(ServiceReference serviceRef, SearchParameter search) {
        if (serviceRef.getServiceId().equals(new URI(NodeService.class.getSimpleName()))) {
            try {
                this.detectedServices.add(new NodeServiceProxy(serviceRef.getService()));
            } catch (TimeoutException e) {
                this.numberOfTimeOuts++;
            }
        }
    }

    /**
     * Get an actual list of detected services.
     * @return the list of detected services
     */
    public List<NodeServiceProxy> getDetectedServices() {
        return new CopyOnWriteArrayList<NodeServiceProxy>(this.detectedServices);
    }

    /**
     * Clear an actual list of detected services.
     */
    public void clearDetectedServices() {
        this.detectedServices.clear();
    }

    /**
     * Get an actual number of time-outs.
     * @return the actual number of time-outs
     */
    public int getNumberOfTimeouts() {
        return this.numberOfTimeOuts;
    }

    /**
     * Get a map of all nodes of actually available (detected) services.
     * @return the map of nodes of detected services
     * @throws InvocationException thrown to indicate that a declared fault occurred during execution of this operation's business logic; clients can extract further fault-related information from this exception, such as user-defined data attached to it 
     * @throws TimeoutException in case invoking an operation of a remote service times out
     */
    public MapOfNodes getMapOfNodesForDetectedServices() throws InvocationException, TimeoutException {
        MapOfNodes result = new MapOfNodes();
        // detectedServices cannot be accessed direcly due to cuncurrency, so we use thread-safe getDetectedServices()
        for (NodeServiceProxy nodeServiceProxy : this.getDetectedServices()) {
            Node node = nodeServiceProxy.getNodeObject();
            result.put(node.self.getID(), node);
        }
        return result;
    }
}
