package eu.esonia.but.geoloc4d.rest;

import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.Vector3D;
import java.io.IOException;
import org.json.JSONException;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

/**
 * Basic RESTlet class for forwarding request to a node's webservice.
 *
 * @author rychly
 */
public class NodeRestlet extends Restlet implements NodeRestletInterface {

    public static final String ATTRIBUTE = "property";
    public static final String ATTRIBUTE_info = "info";
    public static final String ATTRIBUTE_scan = "scan";
    public static final String ATTRIBUTE_locationAbsolute = "locationAbsolute";
    /**
     * Data of node where is restlet
     */
    private Node node;

    /**
     * Construct NodeRestlet object from a node of a network with geolocation
     * ability.
     *
     * @param restletNode the node to construct from
     */
    public NodeRestlet(Node restletNode) {
        super();
        this.node = restletNode;
    }

    /**
     * Construct NodeRestlet object from a node of a network with geolocation
     * ability.
     *
     * @param restletNode the node to construct from
     * @param context the context of the Restlet
     */
    public NodeRestlet(Node restletNode, Context context) {
        super(context);
        this.node = restletNode;
    }

    @Override
    public String get() {
        return this.node.toJSONString();
    }

    @Override
    public String getInfo() {
        return this.node.getInfo().toJSONString();
    }

    @Override
    public String getScan() {
        return this.node.getScan().toJSONString();
    }

    @Override
    public String getLocationAbsolute() {
        return this.node.getInfo().getLocationAbsolute().toJSONString();
    }

    @Override
    public void postLocationAbsolute(String location) throws JSONException {
        this.node.getInfo().setLocationAbsolute(new Vector3D(location));
    }

    /**
     * Handles a call of RESTlet.
     *
     * @param request the request to handle
     * @param response the response to update
     */
    @Override
    public void handle(Request request, Response response) {
        // call super before adding our own logic
        super.handle(request, response);
        // check attributes of the RESTlet
        if (request.getAttributes().containsKey(NodeRestlet.ATTRIBUTE)) {
            // the RESTlet has been called as a resource with a specific property
            // analyse URL
            String property =
                    request.getAttributes().get(NodeRestlet.ATTRIBUTE).toString();
            if (property.equalsIgnoreCase(NodeRestlet.ATTRIBUTE_info)) {
                // URL ends with "/info"
                response.setEntity(this.getInfo(), MediaType.TEXT_PLAIN);
            } else if (property.equalsIgnoreCase(NodeRestlet.ATTRIBUTE_scan)) {
                // URL ends with "/scan"
                response.setEntity(this.getScan(), MediaType.TEXT_PLAIN);
            } else if (property.equalsIgnoreCase(NodeRestlet.ATTRIBUTE_locationAbsolute)) {
                // URL ends with "/locationAbsolute"
                if (request.getMethod() == Method.POST) {
                    // and HTTP method is POST
                    try {
                        this.postLocationAbsolute(request.getEntity().getText());
                    }
                    catch (IOException | JSONException ex) {
                        response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
                                ex.getMessage());
                    }
                } else {
                    // and HTTP method is not POST (e.g. GET)
                    response.setEntity(this.getLocationAbsolute(), MediaType.TEXT_PLAIN);
                }
            } else {
                // URL ends with "/info"
                response.setStatus(Status.CLIENT_ERROR_NOT_FOUND,
                        "Unknown property '" + property + "' of the accessed resource!");
            }
        } else {
            // the RESTlet has been called as a whole resource
            response.setEntity(this.get(), MediaType.TEXT_PLAIN);
        }
    }

    /**
     * Get identificator of the RESTlet's node (e.g. its name).
     *
     * @return identificator of the node
     */
    public String getID() {
        return this.node.getInfo().getID();
    }
}