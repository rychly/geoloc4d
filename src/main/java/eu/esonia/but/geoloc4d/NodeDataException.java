package eu.esonia.but.geoloc4d;

/**
 * Exception NodeData.set of when fail to parse the string representation.
 * @author rychly
 */
public class NodeDataException extends IllegalArgumentException {

    NodeDataException(String message) {
        super(message);
    }
}
