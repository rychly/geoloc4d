package eu.esonia.but.geoloc4d.dpws;

import java.util.Timer;
import java.util.TimerTask;
import org.ws4d.java.client.SearchParameter;

/**
 * Allows to run {@link NodeServiceDetector#searchNodeService(org.ws4d.java.client.SearchParameter)}
 * periodically.
 *
 * @author rychly
 */
public class NodeServiceDetectorHeartbeat extends TimerTask {

    /**
     * The node service detector to be executed on the heratbeat.
     */
    private NodeServiceDetector nodeServiceDetector;
    /**
     * The initial search parameter.
     */
    private SearchParameter initialSearchParameter;
    /**
     * The timer for the heatbeat (null if not active).
     */
    private Timer timer;

    /**
     * Construct a heartbeat for given node service detector.
     *
     * @param nodeServiceDetector the node service detector to be executed on
     * the heratbeat
     */
    public NodeServiceDetectorHeartbeat(NodeServiceDetector nodeServiceDetector) {
        this(nodeServiceDetector, null);
    }

    /**
     * Construct a heartbeat for given node service detector and optional
     * initial search parameter.
     *
     * @param nodeServiceDetector the node service detector to be executed on
     * the heratbeat
     * @param initialSearchParameter the optional initial search parameter (null
     * if empty)
     */
    public NodeServiceDetectorHeartbeat(NodeServiceDetector nodeServiceDetector, SearchParameter initialSearchParameter) {
        this.nodeServiceDetector = nodeServiceDetector;
        this.initialSearchParameter = initialSearchParameter;
        this.timer = null;
    }

    @Override
    public void run() {
        this.nodeServiceDetector.searchNodeService(initialSearchParameter);
    }

    /**
     * Schedules {@link NodeServiceDetector#searchNodeService(org.ws4d.java.client.SearchParameter)}
     * for repeated fixed-delay execution (approximately regular intervals
     * separated by the specified period).
     *
     * @param timeInterval time in milliseconds between successive task
     * executions.
     */
    public void schedule(int timeInterval) {
        this.stop();
        this.timer = new Timer();
        this.timer.schedule(this, 0, timeInterval);
    }

    /**
     * Stops (cancels) a previously scheduled {@link NodeServiceDetector#searchNodeService(org.ws4d.java.client.SearchParameter)}.
     */
    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }
}
