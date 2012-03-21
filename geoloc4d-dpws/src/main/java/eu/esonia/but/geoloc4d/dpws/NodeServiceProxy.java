package eu.esonia.but.geoloc4d.dpws;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.Service;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.types.QName;
import org.ws4d.java.util.ParameterUtil;

/**
 * Proxy for a remote service of DPWS representing a network node and its data
 * (e.g. location).
 *
 * @author rychly
 */
public class NodeServiceProxy implements NodeServiceInterface {

    /**
     * The remote service accessed by the proxy.
     */
    private Service remoteService;

    /**
     * Construct the proxy to given remote service of type NodeService.
     *
     * @param remoteService
     */
    public NodeServiceProxy(Service remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public String getInfo() throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_getInfo);
        // now lets invoke the operation (without any input parameters)
        ParameterValue result = operation.invoke(null);
        // return result from the operation's output
        return ParameterUtil.getString(result, null);
    }

    /**
     * Get basic data of a node as a NodeData object.
     *
     * @return the basic data of a node as a NodeData object
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     * @throws JSONException fail to parse the string representation in JSON
     */
    public NodeData getInfoObject() throws InvocationException, TimeoutException, JSONException {
        return new NodeData(new JSONObject(this.getInfo()));
    }

    @Override
    public String getScan() throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_getScan);
        // now lets invoke the operation (without any input parameters)
        ParameterValue result = operation.invoke(null);
        // return result from the operation's output
        return ParameterUtil.getString(result, null);
    }

    /**
     * Get neighbouring nodes (from scan) as a MapOfNeighbours object.
     *
     * @return a list of the neighbouring nodes as a MapOfNeighbours object
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     * @throws JSONException fail to parse a neighbour's representation in JSON
     */
    public MapOfNeighbours getScanObject() throws InvocationException, TimeoutException, JSONException {
        return new MapOfNeighbours(new JSONArray(this.getScan()));
    }

    @Override
    public void setLocationAbsolute(String location) throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_setLocationAbsolute);
        // prepare an input parameter's value
        ParameterValue input = operation.createInputValue();
        ParameterUtil.setString(input, null, location);
        // now lets invoke the operation (with the input parameter)
        operation.invoke(input);
    }

    /**
     * Set node's location from given Vector3D object.
     *
     * @param location the node's location to set in a string representation
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     */
    public void setLocationAbsolute(Vector3D location) throws InvocationException, TimeoutException {
        this.setLocationAbsolute(location.toJSONString());
    }

    @Override
    public String toString() {
        return remoteService.toString();
    }

    /**
     * Get a whole nodes as a Node object.
     *
     * @param includingScan query also neighbours of the node
     * @return a Node object
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     * @throws JSONException error in parsing of JSON string representation
     */
    public Node getNodeObject(boolean includingScan) throws InvocationException, TimeoutException, JSONException {
        MapOfNeighbours neighbours = null;
        if (includingScan) {
            neighbours = this.getScanObject();
        }
        return new Node(this.getInfoObject(), neighbours);
    }
}
