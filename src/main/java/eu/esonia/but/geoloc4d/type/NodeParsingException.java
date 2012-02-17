package eu.esonia.but.geoloc4d.type;

/**
 * Exception {@link NodeData#set(java.lang.String)} of when fail to parse the
 * string representation.
 *
 * @author rychly
 */
public class NodeParsingException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    NodeParsingException(String message) {
        super(message);
    }
}
