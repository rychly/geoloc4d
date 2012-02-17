package eu.esonia.but.geoloc4d.util;

/**
 * To create new instances of specific trilateration strategies.
 * TrilaterationStrategyFactory design pattern.
 *
 * @author rychly
 */
public final class TrilaterationStrategyFactory {

    /**
     * Make a new instance of a specific trilateration strategy based on RSSI.
     *
     * @return the trilateration strategy
     */
    public static TrilaterationStrategy newStrategyWithRSSI() {
        return new StrategyWithRSSI();
    }

    /**
     * Make a new instance of a specific trilateration strategy based on RTT.
     *
     * @return the trilateration strategy
     */
    public static TrilaterationStrategy newStrategyWithRTT() {
        return new StrategyWithRTT();
    }
}
