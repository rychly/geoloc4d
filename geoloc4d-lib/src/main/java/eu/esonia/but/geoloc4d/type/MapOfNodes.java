package eu.esonia.but.geoloc4d.type;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONString;
import org.json.JSONTokener;

/**
 * Map of neighbouring nodes as a hash-map.
 *
 * @author rychly
 */
public final class MapOfNodes extends LinkedHashMap<String, Node> implements JSONString {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor of an empty map.
     */
    public MapOfNodes() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public MapOfNodes(final MapOfNodes source) {
        super();
        if (source != null) {
            this.putAll(source);
        }
    }

    /**
     * Default constructor of a map from its string representation in JSON.
     *
     * @param representation string representation of the map in JSON
     * @throws JSONException fail to parse a node's string representation in
     * JSON
     */
    public MapOfNodes(final String representation) throws JSONException {
        this(new JSONArray(representation));
    }

    /**
     * Constructor of a map from its representation in JSONArray.
     *
     * @param representation representation of the map in JSONArray
     * @throws JSONException fail to parse a node's representation in JSON
     */
    public MapOfNodes(final JSONArray representation) throws JSONException {
        super();
        for (int i = 0; i < representation.length(); i++) {
            Node node = new Node(representation.getJSONObject(i));
            this.put(node.getInfo().getID(), node);
        }
    }

    public JSONArray toJSONArray() {
        JSONArray result = new JSONArray();
        for (Node node : this.values()) {
            result.put(node.toJSONObject());
        }
        return result;
    }

    @Override
    public String toString() {
        try {
            return this.toJSONArray().toString(1);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be an invalid number!", ex);
        }
    }

    @Override
    public String toJSONString() {
        return this.toJSONArray().toString();
    }

    /**
     * Loads a map of nodes from a file with its JSON representation.
     *
     * @param filename the file to read from
     * @throws FileNotFoundException the file not found
     * @throws IOException a read error of the file
     * @throws JSONException parsing error in the JSON represenatation
     * @return the map of nodes
     */
    public static MapOfNodes loadNodes(final String filename)
            throws FileNotFoundException, IOException, JSONException {
        try (FileInputStream stream = new FileInputStream(filename)) {
            return loadNodes(stream);
        }
    }

    /**
     * Loads a map of nodes from a file with its JSON representation and the
     * nodes' IDs modified by given replacement.
     *
     * @param filename the file to read from
     * @param idReplacement the replacement string where a node's ID will be
     * inserted instead of "%" character
     * @throws FileNotFoundException the file not found
     * @throws IOException a read error of the file
     * @throws JSONException parsing error in the JSON represenatation
     * @return the map of nodes
     */
    public static MapOfNodes loadNodes(final String filename, final String idReplacement)
            throws FileNotFoundException, IOException, JSONException {
        try (FileInputStream stream = new FileInputStream(filename)) {
            return loadNodes(stream, idReplacement);
        }
    }

    /**
     * Loads a map of nodes from an input stream with its JSON representation.
     *
     * @param stream the input stream to read from
     * @return the map of nodes
     * @throws IOException a read error of the input stream
     * @throws JSONException parsing error in the JSON represenatation
     */
    public static MapOfNodes loadNodes(final InputStream stream)
            throws IOException, JSONException {
        try (InputStreamReader streamReader = new InputStreamReader(stream)) {
            return new MapOfNodes(new JSONArray(new JSONTokener(streamReader)));
        }
    }

    /**
     * Loads a map of nodes from an input stream with its JSON representation
     * and the nodes' IDs modified by given replacement.
     *
     * @param stream the input stream to read from
     * @param idReplacement the replacement string where a node's ID will be
     * inserted instead of "%" character
     * @return the map of nodes
     * @throws IOException a read error of the input stream
     * @throws JSONException parsing error in the JSON represenatation
     */
    public static MapOfNodes loadNodes(final InputStream stream, final String idReplacement)
            throws IOException, JSONException {
        // convert the input stream into UTF-8 encoded string
        Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";
        // create a map of the nodes from the string with regexp replaced the nodes' IDs
        return new MapOfNodes(new JSONArray(jsonString.replaceAll(
                "(?i)(\"id\"\\s*:\\s*)\"([^\"])*\"",
                "$1\"" + idReplacement.replace("%", "$2") + "\"")));
    }

    /**
     * Save the map of nodes into a file.
     *
     * @param filename the file to save into
     * @throws IOException a write error to the file
     */
    public void saveNodes(final String filename)
            throws IOException {
        try (OutputStream stream = new FileOutputStream(filename)) {
            saveNodes(stream);
        }
    }

    /**
     * Save the map of nodes into an output stream as JSON.
     *
     * @param stream the output stream to save into
     * @throws IOException a write error to the stream
     */
    public void saveNodes(final OutputStream stream) throws IOException {
        try (OutputStreamWriter outputWriter = new OutputStreamWriter(stream)) {
            try {
                this.toJSONArray().write(outputWriter);
            }
            catch (JSONException ex) {
                throw new RuntimeException("Impossible, the value cannot be an invalid number!", ex);
            }
        }
    }

    /**
     * Check if all nodes in the map have set their absolute location.
     *
     * @return true iff the all nodes in the map are localised
     */
    public boolean isCompletelyLocalised() {
        for (Node node : this.values()) {
            if (!node.getInfo().isAbsolutelyLocalised()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a map of the nodes with or without defined location.
     *
     * @param localised select only localised (true) or only unlocalised (false)
     * nodes
     * @return the map of the nodes with or without defined location
     */
    public MapOfNodes getNodesByLocation(final boolean localised) {
        MapOfNodes result = new MapOfNodes();
        for (Map.Entry<String, Node> pair : this.entrySet()) {
            boolean isAbsolutelyLocalised = pair.getValue().getInfo().isAbsolutelyLocalised();
            if (( localised && isAbsolutelyLocalised ) || ( !localised && !isAbsolutelyLocalised )) {
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return result;
    }
}
