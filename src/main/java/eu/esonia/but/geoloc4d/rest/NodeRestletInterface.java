package eu.esonia.but.geoloc4d.rest;

import java.io.IOException;
import org.json.JSONException;
import org.restlet.resource.ResourceException;

/**
 * Interface for core resources of a restler representing a network node and its
 * data (e.g. location).
 *
 * @author rychly
 */
public interface NodeRestletInterface {

    /**
     * Get the all (basic data and scan) of a node as a JSONObject string.
     *
     * @return the all info about the node as a toJSONString result
     * @throws IOException error in the HTTP query if needed
     * @throws ResourceException error when accessing the remote resource
     */
    public String get()
            throws IOException, ResourceException;

    /**
     * Get basic data of a node as a JSONObject string.
     *
     * @return the basic data of a node as a toJSONString result
     * @throws IOException error in the HTTP query if needed
     * @throws ResourceException error when accessing the remote resource
     */
    public String getInfo()
            throws IOException, ResourceException;

    /**
     * Get neighbouring nodes (from scan) as a JSONArray string.
     *
     * @return a list of the neighbouring nodes as a toJSONString result
     * @throws IOException error in the HTTP query if needed
     * @throws ResourceException error when accessing the remote resource
     */
    public String getScan()
            throws IOException, ResourceException;

    /**
     * Get node's location from given JSONArray[3] string.
     *
     * @return the node's location as a toJSONString result
     * @throws IOException error in the HTTP query if needed
     * @throws ResourceException error when accessing the remote resource
     */
    public String getLocationAbsolute()
            throws IOException, ResourceException;

    /**
     * Set node's location from given JSONArray[3] string.
     *
     * @param location the node's location to set in a string representation
     * @throws IOException error in the HTTP query if needed
     * @throws ResourceException error when accessing the remote resource
     * @throws JSONException fail to parse the location's vector in JSON
     */
    public void postLocationAbsolute(final String location)
            throws IOException, ResourceException, JSONException;
}