package eu.esonia.but.geoloc4d.service;

import org.ws4d.java.service.Operation;
import org.ws4d.java.types.QName;

/**
 * Operation with external implementation as an abstract class.
 * @param <NodeServiceClass> class for object with the external implementation
 * @author rychly
 */
public abstract class NodeServiceOperation<NodeServiceClass> extends Operation {

    /**
     * The external implemenation of the operation.
     */
    private NodeServiceClass implementation;

    /**
     * Create operation with defined name and external implementation.
     * With the defined name of the operation, there is associated service NodeService and its namespace (this is needed for method setServiceTypes of SearchParameter).
     * @param name the name of the operation
     * @param implementation the external implemenation
     */
    public NodeServiceOperation(String name, NodeServiceClass implementation) {
        super(name, new QName(NodeService.class.getSimpleName(), NodeService.NAMESPACE));
        this.implementation = implementation;
    }

    /**
     * Get the external implemenation of the operation.
     * @return the implementation
     */
    public NodeServiceClass getImplementation() {
        return implementation;
    }
}
