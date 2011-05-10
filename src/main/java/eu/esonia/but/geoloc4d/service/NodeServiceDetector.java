package eu.esonia.but.geoloc4d.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ws4d.java.client.DefaultClient;
import org.ws4d.java.client.SearchParameter;
import org.ws4d.java.communication.TimeoutException;
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
        return Collections.unmodifiableList(this.detectedServices);
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
}
