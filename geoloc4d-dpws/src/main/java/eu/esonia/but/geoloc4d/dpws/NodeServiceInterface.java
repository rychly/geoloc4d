package eu.esonia.but.geoloc4d.dpws;

import org.json.JSONException;
import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.service.InvocationException;

/**
 * Interface for core methods of proxy and service of DPWS representing a
 * network node and its data (e.g. location).
 *
 * @author rychly
 */
public interface NodeServiceInterface {

    /**
     * Get basic data of a node as a JSONObject string.
     *
     * @return the basic data of a node as a toJSONString result
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     */
    public String getInfo() throws InvocationException, TimeoutException;

    /**
     * Get neighbouring nodes (from scan) as a JSONArray string.
     *
     * @return a list of the neighbouring nodes as a toJSONString result
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     */
    public String getScan() throws InvocationException, TimeoutException;

    /**
     * Set node's location from given JSONArray[3] string.
     *
     * @param location the node's location to set in a string representation
     * @throws InvocationException thrown to indicate that a declared fault
     * occurred during execution of this operation's business logic; clients can
     * extract further fault-related information from this exception, such as
     * user-defined data attached to it
     * @throws TimeoutException in case invoking an operation of a remote
     * service times out
     * @throws JSONException fail to parse the location's vector in JSON
     */
    public void setLocationAbsolute(String location) throws InvocationException, TimeoutException, JSONException;

    /**
     * Returns a string representation of the service.
     *
     * @return the string representation of the service
     */
    @Override
    public String toString();
}
