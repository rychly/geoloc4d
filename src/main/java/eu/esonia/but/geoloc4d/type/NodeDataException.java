package eu.esonia.but.geoloc4d.type;

/**
 * Exception NodeData.set of when fail to parse the string representation.
 * @author rychly
 */
public class NodeDataException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    NodeDataException(String message) {
        super(message);
    }
}
