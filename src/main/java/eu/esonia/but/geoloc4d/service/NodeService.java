package eu.esonia.but.geoloc4d.service;

import eu.esonia.but.geoloc4d.type.MapOfNeighbours;
import eu.esonia.but.geoloc4d.type.Node;
import eu.esonia.but.geoloc4d.type.NodeData;
import eu.esonia.but.geoloc4d.type.Vector3D;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.schema.Element;
import org.ws4d.java.schema.SchemaUtil;
import org.ws4d.java.schema.Type;
import org.ws4d.java.service.DefaultService;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.util.ParameterUtil;

/**
 * Service of DPWS representing a network node and its data (e.g. location).
 *
 * @author rychly
 */
public class NodeService extends DefaultService implements NodeServiceInterface {

    public static final String NAMESPACE = "http://geoloc4d.sf.net/";
    public static final String OPERATION_getNodeData = "getNodeData";
    public static final String OPERATION_getNeighbours = "getNeighbours";
    public static final String OPERATION_setNodeLocation = "setNodeLocation";
    /**
     * Data of node where is service
     */
    private Node node;

    /**
     * Construct NodeService object from a node of a network with geolocation
     * ability.
     *
     * @param serviceNode the node to construct from
     */
    public NodeService(Node serviceNode) {
        // we have no configuraion for this service in config. file
        this(-1, serviceNode);
    }

    /**
     * Construct NodeService object from a node of a network with geolocation
     * ability and configuration with given ID.
     *
     * @param serviceConfigID ID of the service's configuration (-1 to ignore)
     * @param serviceNode the node to construct from
     */
    public NodeService(int serviceConfigID, Node serviceNode) {
        // if (id == -1) then do configure this service from config. file
        super(serviceConfigID);
        // init properties
        this.node = serviceNode;
        // add operations
        Type xsString = SchemaUtil.getSchemaType(SchemaUtil.TYPE_STRING);
        Operation getNodeData = new NodeServiceOperation<NodeService>(OPERATION_getNodeData, this) {

            @Override
            public ParameterValue invoke(ParameterValue parameterValue)
                    throws InvocationException, TimeoutException {
                return this.getImplementation().getNodeData(this, parameterValue);
            }
        };
        getNodeData.setOutput(new Element(NodeData.class.getSimpleName(), NodeService.NAMESPACE, xsString));
        this.addOperation(getNodeData);
        Operation getNeighbours = new NodeServiceOperation<NodeService>(OPERATION_getNeighbours, this) {

            @Override
            public ParameterValue invoke(ParameterValue parameterValue)
                    throws InvocationException, TimeoutException {
                return this.getImplementation().getNeighbours(this, parameterValue);
            }
        };
        getNeighbours.setOutput(new Element(MapOfNeighbours.class.getSimpleName(), NodeService.NAMESPACE, xsString));
        this.addOperation(getNeighbours);
        Operation setNodeLocation = new NodeServiceOperation<NodeService>(OPERATION_setNodeLocation, this) {

            @Override
            public ParameterValue invoke(ParameterValue parameterValue)
                    throws InvocationException, TimeoutException {
                return this.getImplementation().setNodeLocation(this, parameterValue);
            }
        };
        setNodeLocation.setInput(new Element(Vector3D.class.getSimpleName(), NodeService.NAMESPACE, xsString));
        this.addOperation(setNodeLocation);
    }

    @Override
    public String getNodeData() {
        return this.node.self.toString();
    }

    @Override
    public String getNeighbours() {
        return this.node.scan.toString();
    }

    @Override
    public void setNodeLocation(String location) {
        this.node.self.getLocationAbsolute().set(location);
    }

    /**
     * Get basic data of a node.
     *
     * @param operation related web-service operation
     * @param parameterValue input parameters of the operation
     * @return output parameters of the operation
     * @throws InvocationException when the operation cannot be performed
     * @throws TimeoutException when the operation time-outs
     */
    protected ParameterValue getNodeData(NodeServiceOperation<NodeService> operation, ParameterValue parameterValue)
            throws InvocationException, TimeoutException {

        ParameterValue result = operation.createOutputValue();
        ParameterUtil.setString(result, null, this.getNodeData());
        return result;
    }

    /**
     * Get neighbouring nodes (from scan).
     *
     * @param operation related web-service operation
     * @param parameterValue input parameters of the operation
     * @return output parameters of the operation
     * @throws InvocationException when the operation cannot be performed
     * @throws TimeoutException when the operation time-outs
     */
    protected ParameterValue getNeighbours(NodeServiceOperation<NodeService> operation, ParameterValue parameterValue)
            throws InvocationException, TimeoutException {
        ParameterValue result = operation.createOutputValue();
        ParameterUtil.setString(result, null, this.getNeighbours());
        return result;
    }

    /**
     * Set node's location.
     *
     * @param operation related web-service operation
     * @param parameterValue input parameters of the operation
     * @return output parameters of the operation
     * @throws InvocationException when the operation cannot be performed
     * @throws TimeoutException when the operation time-outs
     */
    protected ParameterValue setNodeLocation(NodeServiceOperation<NodeService> operation, ParameterValue parameterValue)
            throws InvocationException, TimeoutException {
        this.setNodeLocation(ParameterUtil.getString(parameterValue, null));
        return null;
    }
}
