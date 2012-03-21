package eu.esonia.but.geoloc4d.rest;

import eu.esonia.but.geoloc4d.type.MapOfNodes;
import eu.esonia.but.geoloc4d.type.NeighbourProperties;
import eu.esonia.but.geoloc4d.type.Node;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.json.JSONException;
import org.restlet.Client;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

/**
 * Class that gathers and categorizes RESTlets in a network of wireless nodes.
 *
 * @author rychly
 */
public class NodeRestletCrawler {

    /**
     * The actual map of gathered RESTlets.
     */
    private Map<URI, NodeRestletProxy> gatheredRestlets;
    /**
     * The client connector for a client resource common for all RESTlets.
     */
    private Client clientConnector;

    /**
     * Construct a crawler starting with given URIs of RESTlets.
     *
     * @param uris the given URIs of RESTlets to crawl
     */
    public NodeRestletCrawler(final URI[] uris) {
        this(null, uris);
    }

    /**
     * Construct a crawler starting with given URIs of RESTlets.
     *
     * @param stringUris the given URIs of RESTlets to crawl
     */
    public NodeRestletCrawler(final String[] stringUris) {
        this(null, stringUris);
    }

    /**
     * Construct a crawler starting with given URIs of RESTlets.
     *
     * @param clientConnector the client connector for a client resource common
     * for the URIs
     * @param uris the given URIs of RESTlets to crawl
     */
    public NodeRestletCrawler(final Client clientConnector, final URI[] uris) {
        this.init(clientConnector, uris);
    }

    /**
     * Construct a crawler starting with given URIs of RESTlets.
     *
     * @param clientConnector the client connector for a client resource common
     * for the URIs
     * @param stringUris the given URIs of RESTlets to crawl
     */
    public NodeRestletCrawler(final Client clientConnector, final String[] stringUris) {
        URI uris[] = new URI[stringUris.length];
        for (int i = 0; i < stringUris.length; i++) {
            try {
                uris[i] = new URI(stringUris[i]);
            }
            catch (URISyntaxException ex) {
                uris[i] = null;
            }
        }
        this.init(clientConnector, uris);
    }

    private void init(final Client clientConnector, final URI[] uris) {
        this.clientConnector = clientConnector;
        this.gatheredRestlets = new LinkedHashMap<>();
        for (URI uri : uris) {
            this.addNodeRestletProxy(uri);
        }
    }

    /**
     * Add a new node with given URI into the gathered nodes.
     *
     * @param uri the given URI of the node's RESTlet
     */
    private NodeRestletProxy addNodeRestletProxy(final URI uri) {
        if (uri == null) {
            return null;
        } else if (!this.gatheredRestlets.containsKey(uri)) {
            // put into the gathered if does not exists yet
            ClientResource clientResource = new ClientResource(uri);
            if (this.clientConnector != null) {
                // set the client connector if any
                clientResource.setNext(this.clientConnector);
            }
            NodeRestletProxy nodeRestletProxy = new NodeRestletProxy(clientResource);
            this.gatheredRestlets.put(uri, nodeRestletProxy);
            return nodeRestletProxy;
        } else {
            // return existing with the same URI if any
            return this.gatheredRestlets.get(uri);
        }
    }

    /**
     * Crawl gathered nodes and extend them by the newly obtained.
     */
    public void crawlGatheredNodes() {
        // go throught all the gathered RESTlets' proxies
        for (Queue<NodeRestletProxy> queue = new LinkedList<>(this.gatheredRestlets.values());
                !queue.isEmpty();) {
            // dequeue the first proxy
            NodeRestletProxy nodeRestletProxy = queue.poll();
            try {
                // get URIs of RESTlets of neighbouring nodes of the proxied RESTlet
                for (NeighbourProperties neighbourProperties : nodeRestletProxy.getScanObject().values()) {
                    URI uri = neighbourProperties.getURI();
                    if (( uri != null ) && !this.gatheredRestlets.containsKey(uri)) {
                        // if a new URI sucessfully obtained, add to the gathered ...
                        NodeRestletProxy newNodeRestletProxy =
                                this.addNodeRestletProxy(uri);
                        // ... and enque a new RESTlet with the URI
                        queue.add(newNodeRestletProxy);
                    }
                }
            }
            catch (IOException | ResourceException | JSONException ex) {
                // skip unaccesible URIs
            }
        }
    }

    /**
     * Get an actual set of gathered RESTlets.
     *
     * @return the set of gathered RESTlets
     */
    public Set<NodeRestletProxy> getGatheredRestlets() {
        return new HashSet<>(this.gatheredRestlets.values());
    }

    /**
     * Get a map of all nodes of actually gathered RESTlets.
     *
     * @param includingNeighbours query also neighbours of the RESTlets' nodes
     * @return the map of all nodes of actually gathered RESTlets
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException error in parsing of JSON string representation
     */
    public MapOfNodes getMapOfNodesForGatheredRestlets(final boolean includingNeighbours)
            throws IOException, ResourceException, JSONException {
        MapOfNodes result = new MapOfNodes();
        for (NodeRestletProxy nodeRestletProxy : this.gatheredRestlets.values()) {
            Node node = nodeRestletProxy.getNodeObject(includingNeighbours);
            result.put(node.getInfo().getID(), node);
        }
        return result;
    }
}