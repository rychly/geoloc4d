package eu.esonia.but.geoloc4d.util;

/**
 * Exception for WirelessMetric, e.g. when parameters do not define a possible metric.
 * @author rychly
 */
public class WirelessMetricException extends Exception {

    private static final long serialVersionUID = 1L;

    WirelessMetricException(String message) {
        super(message);
    }
}
