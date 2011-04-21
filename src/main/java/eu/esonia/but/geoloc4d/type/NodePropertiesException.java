package eu.esonia.but.geoloc4d.type;

/**
 * Exception NodeProperties.set of when fail to parse the string representation.
 * @author rychly
 */
public class NodePropertiesException extends IllegalArgumentException {

    NodePropertiesException(String message) {
        super(message);
    }
}
