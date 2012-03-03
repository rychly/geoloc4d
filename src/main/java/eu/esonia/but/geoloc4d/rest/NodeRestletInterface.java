package eu.esonia.but.geoloc4d.rest;

import org.json.JSONException;

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
     */
    public String get();

    /**
     * Get basic data of a node as a JSONObject string.
     *
     * @return the basic data of a node as a toJSONString result
     */
    public String getInfo();

    /**
     * Get neighbouring nodes (from scan) as a JSONArray string.
     *
     * @return a list of the neighbouring nodes as a toJSONString result
     */
    public String getScan();

    /**
     * Get node's location from given JSONArray[3] string.
     *
     * @return the node's location as a toJSONString result
     */
    public String getLocationAbsolute();

    /**
     * Set node's location from given JSONArray[3] string.
     *
     * @param location the node's location to set in a string representation
     * @throws JSONException fail to parse the location's vector in JSON
     */
    public void postLocationAbsolute(String location) throws JSONException;
}