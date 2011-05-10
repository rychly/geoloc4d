package eu.esonia.but.geoloc4d.service;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.Service;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.types.QName;
import org.ws4d.java.util.ParameterUtil;

/**
 * Proxy for a remote service of DPWS representing a network node and its data (e.g. location).
 * @author rychly
 */
public class NodeServiceProxy implements NodeServiceInterface {

    /**
     * The remote service accessed by the proxy.
     */
    private Service remoteService;

    /**
     * Construct the proxy to given remote service of type NodeService.
     * @param remoteService
     */
    public NodeServiceProxy(Service remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public String getNodeData() throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_getNodeData);
        // now lets invoke the operation (without any input parameters)
        ParameterValue result = operation.invoke(null);
        // return result from the operation's output
        return ParameterUtil.getString(result, null);
    }

    /**
     * Get basic data of a node as a NodeData object.
     * @return the basic data of a node as a NodeData object
     * @throws InvocationException thrown to indicate that a declared fault occurred during execution of this operation's business logic; clients can extract further fault-related information from this exception, such as user-defined data attached to it 
     * @throws TimeoutException in case invoking an operation of a remote service times out
     */
    public NodeData getNodeDataObject() throws InvocationException, TimeoutException {
        return new NodeData(this.getNodeData());
    }

    @Override
    public String getNeighbours() throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_getNeighbours);
        // now lets invoke the operation (without any input parameters)
        ParameterValue result = operation.invoke(null);
        // return result from the operation's output
        return ParameterUtil.getString(result, null);
    }

    /**
     * Get neighbouring nodes (from scan) as a MapOfNeighbours object.
     * @return a list of the neighbouring nodes as a MapOfNeighbours object
     * @throws InvocationException thrown to indicate that a declared fault occurred during execution of this operation's business logic; clients can extract further fault-related information from this exception, such as user-defined data attached to it 
     * @throws TimeoutException in case invoking an operation of a remote service times out
     */
    public MapOfNeighbours getNeighboursObject() throws InvocationException, TimeoutException {
        return new MapOfNeighbours(this.getNeighbours());
    }

    @Override
    public void setNodeLocation(String location) throws InvocationException, TimeoutException {
        // We need to get the related operation from the service.
        // getAnyOperation returns the first Operation that fits the specification in the parameters.
        Operation operation = this.remoteService.getAnyOperation(
                new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE),
                NodeService.OPERATION_setNodeLocation);
        // prepare an input parameter's value
        ParameterValue input = operation.createInputValue();
        ParameterUtil.setString(input, null, location);
        // now lets invoke the operation (with the input parameter)
        operation.invoke(input);
    }

    /**
     * Set node's location from given Vector3D object.
     * @param location the node's location to set in a string representation
     * @throws InvocationException thrown to indicate that a declared fault occurred during execution of this operation's business logic; clients can extract further fault-related information from this exception, such as user-defined data attached to it 
     * @throws TimeoutException in case invoking an operation of a remote service times out
     */
    public void setNodeLocation(Vector3D location) throws InvocationException, TimeoutException {
        this.setNodeLocation(location.toString());
    }

    /**
     * Get a whole nodes as a Node object.
     * @return a Node object
     * @throws InvocationException thrown to indicate that a declared fault occurred during execution of this operation's business logic; clients can extract further fault-related information from this exception, such as user-defined data attached to it 
     * @throws TimeoutException in case invoking an operation of a remote service times out
     */
    public Node getNodeObject() throws InvocationException, TimeoutException {
        return new Node(this.getNodeDataObject(), this.getNeighboursObject());
    }
}
