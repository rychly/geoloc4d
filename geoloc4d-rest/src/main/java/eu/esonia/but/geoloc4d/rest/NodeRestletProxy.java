package eu.esonia.but.geoloc4d.rest;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import java.io.IOException;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

/**
 * Proxy for a remote RESTlet representing a network node and its data (e.g.
 * location).
 *
 * @author rychly
 */
public class NodeRestletProxy implements NodeRestletInterface {

    private ClientResource clientResource;

    public NodeRestletProxy(final ClientResource clientResource) {
        this.clientResource = clientResource;
    }

    @Override
    public String get() throws IOException, ResourceException {
        return this.clientResource.
                getChild(NodeRestletApplication.ROOTPATH).
                get(MediaType.TEXT_PLAIN).getText();
    }

    /**
     * Get the all (basic data and scan) of a node as a NodeData object.
     *
     * @return the all info about the node as a NodeData object
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the string representation in JSON
     */
    public Node getNodeObject() throws IOException, ResourceException, JSONException {
        return new Node(new JSONObject(this.get()));
    }

    /**
     * Get the all (basic data and optionally also scan) of a node as a NodeData
     * object.
     *
     * @param includingScan query also neighbours of the node
     * @return the all info about the node as a NodeData object
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the string representation in JSON
     */
    public Node getNodeObject(final boolean includingScan)
            throws IOException, ResourceException, JSONException {
        if (includingScan) {
            return this.getNodeObject();
        } else {
            return new Node(this.getInfoObject(), null);
        }
    }

    @Override
    public String getInfo() throws IOException, ResourceException {
        return this.clientResource.
                getChild(NodeRestletApplication.ROOTPATH + "/" + NodeRestlet.ATTRIBUTE_info).
                get(MediaType.TEXT_PLAIN).getText();
    }

    /**
     * Get basic data of a node as a NodeData object.
     *
     * @return the basic data of a node as a NodeData object
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the string representation in JSON
     */
    public NodeData getInfoObject() throws IOException, ResourceException, JSONException {
        return new NodeData(new JSONObject(this.getInfo()));
    }

    @Override
    public String getScan() throws IOException, ResourceException {
        return this.clientResource.
                getChild(NodeRestletApplication.ROOTPATH + "/" + NodeRestlet.ATTRIBUTE_scan).
                get(MediaType.TEXT_PLAIN).getText();
    }

    /**
     * Get neighbouring nodes (from scan) as a MapOfNeighbours object.
     *
     * @return a list of the neighbouring nodes as a MapOfNeighbours object
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the string representation in JSON
     */
    public MapOfNeighbours getScanObject() throws IOException, ResourceException, JSONException {
        return new MapOfNeighbours(new JSONArray(this.getScan()));
    }

    @Override
    public String getLocationAbsolute() throws IOException, ResourceException {
        return this.clientResource.
                getChild(NodeRestletApplication.ROOTPATH + "/" + NodeRestlet.ATTRIBUTE_locationAbsolute).
                get(MediaType.TEXT_PLAIN).getText();
    }

    /**
     * Get node's location from given Vector3D object.
     *
     * @return the node's location as a Vector3D object
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the string representation in JSON
     */
    public Vector3D getLocationAbsoluteObject() throws IOException, ResourceException, JSONException {
        return new Vector3D(new JSONArray(this.getLocationAbsolute()));
    }

    @Override
    public void postLocationAbsolute(final String location) throws IOException, ResourceException {
        this.clientResource.
                getChild(NodeRestletApplication.ROOTPATH + "/" + NodeRestlet.ATTRIBUTE_locationAbsolute).
                post(location, MediaType.TEXT_PLAIN);
    }

    /**
     * Post node's location from given Vector3D object.
     *
     * @param location the node's location to set in a string representation
     * @throws IOException error in the HTTP query while getting the string
     * representation in JSON
     * @throws ResourceException error when accessing the remote resource
     */
    public void postLocationAbsolute(final Vector3D location) throws IOException, ResourceException {
        this.postLocationAbsolute(location.toJSONString());
    }

    /**
     * Get the context of the proxy's client resource for a remote RESTlet.
     *
     * @return the context of the proxy's client resource
     */
    public Context getContext() {
        return this.clientResource.getContext();
    }

    @Override
    public String toString() {
        return this.clientResource.getReference().toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (!( object instanceof NodeRestletProxy )) {
            return false;
        } else {
            NodeRestletProxy nodeRestletProxy = (NodeRestletProxy) object;
            return this.toString().equals(nodeRestletProxy.toString());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.toString());
        return hash;
    }
}