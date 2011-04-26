package eu.esonia.but.geoloc4d.util;

/**
 * Exception for TrilaterationStrategy algorithms, e.g. when in case of not enought neighbouring nodes.
 * @author rychly
 */
public class TrilaterationStrategyException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    TrilaterationStrategyException(String message) {
        super(message);
    }
}
