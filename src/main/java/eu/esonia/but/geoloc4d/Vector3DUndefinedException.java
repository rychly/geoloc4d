package eu.esonia.but.geoloc4d;

/**
 * Exception on operation with undefined Vector3D.
 * @author rychly
 */
public class Vector3DUndefinedException extends RuntimeException {

    private static final String defaultMessage = "Undefined vector in the operation!";

    Vector3DUndefinedException() {
        super(defaultMessage);
    }

    Vector3DUndefinedException(String message) {
        super(message);
    }
}
