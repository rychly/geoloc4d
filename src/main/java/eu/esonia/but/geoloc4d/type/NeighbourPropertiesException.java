package eu.esonia.but.geoloc4d.type;

/**
 * Exception NodeProperties.set of when fail to parse the string representation.
 * @author rychly
 */
public class NeighbourPropertiesException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    NeighbourPropertiesException(String message) {
        super(message);
    }
}
