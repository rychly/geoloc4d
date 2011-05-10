package eu.esonia.but.geoloc4d.service;

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
}
