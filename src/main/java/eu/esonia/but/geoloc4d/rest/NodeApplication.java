package eu.esonia.but.geoloc4d.rest;

import eu.esonia.but.geoloc4d.type.Node;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Class for a REST application representing a node of a network with
 * geolocation ability.
 *
 * @author rychly
 */
public class NodeApplication extends Application {

    public static final String ROOTPATH = "/geoloc4d";
    /**
     * Data of node where is restlet
     */
    private NodeRestlet nodeRestlet;

    /**
     * Constructor of REST application form a node of a network with geolocation
     * ability.
     *
     * @param restletNode the node to construct from
     */
    public NodeApplication(Node restletNode) {
        super();
        this.nodeRestlet = new NodeRestlet(restletNode);
    }

    /**
     * Constructor of REST application form a node of a network with geolocation
     * ability.
     *
     * @param restletNode the node to construct from
     * @param context the context to use based on parent component context
     */
    public NodeApplication(Node restletNode, Context context) {
        super(context);
        this.nodeRestlet = new NodeRestlet(restletNode, context);
    }

    /**
     * Creates a inbound root Restlet that will receive all incoming calls.
     *
     * @return the inbound root Restlet
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // create a root router Restlet that routes each call to our RESTlet
        Router router = new Router(getContext());
        // attach the handlers to the root router
        router.attach(NodeApplication.ROOTPATH,
                this.nodeRestlet);
        router.attach(NodeApplication.ROOTPATH + "/{" + NodeRestlet.ATTRIBUTE + "}",
                this.nodeRestlet);
        return router;
    }
}
