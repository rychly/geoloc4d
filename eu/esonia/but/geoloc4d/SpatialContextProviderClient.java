package eu.esonia.but.geoloc4d;

import java.util.ArrayList;
import java.util.List;
import org.ws4d.java.client.DefaultClient;
import org.ws4d.java.client.SearchParameter;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.Service;
import org.ws4d.java.service.reference.ServiceReference;
import org.ws4d.java.types.URI;

/**
 * Client for searching of Asset Services.
 * @author rychly
 */
public class SpatialContextProviderClient extends DefaultClient {

    private ArrayList<Service> foundServices;

    SpatialContextProviderClient() {
        this.foundServices = new ArrayList<Service>();
    }

    // called if there are search-results, after triggered method searchService(new SearchParameter())
    @Override
    public void serviceFound(ServiceReference serviceRef, SearchParameter search) {
        if (serviceRef.getServiceId().equals(new URI(AssetService.class.getSimpleName()))) {
            try {
                this.foundServices.add(serviceRef.getService());
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Service> getAvailableServices() {
        return this.foundServices;
    }

    public void clear() {
        this.foundServices.clear();
    }
}
