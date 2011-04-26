package eu.esonia.but.geoloc4d.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Map of neighbouring nodes as a hash-map.
 * @author rychly
 */
public final class MapOfNodes extends LinkedHashMap<String, Node> {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor of an empty map.
     */
    public MapOfNodes() {
        super();
    }

    /**
     * Copy constructor.
     * @param source source to copy from
     */
    public MapOfNodes(final MapOfNodes source) {
        this.set(source);
    }

    /**
     * Default constructor of a map from its string representation.
     * @param representation string representation of the map
     * @throws IOException a read error from the representation
     */
    public MapOfNodes(final String representation) throws IOException {
        this.set(representation);
    }

    /**
     * Add to map content from another map.
     * @param source source scan list
     */
    public void set(final MapOfNodes source) {
        this.putAll(source);
    }

    /**
     * Set map from its string representation. It's reverese operation to toString method.
     * @param representation string representation of map
     * @throws IOException a read error from the representation
     */
    public void set(final String representation) throws IOException {
        this.set(loadNodes(new ByteArrayInputStream(representation.getBytes())));
    }

    @Override
    public String toString() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        this.saveNodes(result);
        return result.toString();
    }

    /**
     * Loads a map of nodes from a file.
     * @param filename the file to read from
     * @throws FileNotFoundException the file not found
     * @throws IOException a read error of the file
     * @return the map of nodes
     */
    public static MapOfNodes loadNodes(final String filename)
            throws FileNotFoundException, IOException {
        InputStream stream = new FileInputStream(filename);
        try {
            return loadNodes(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Loads a map of nodes from an input stream.
     * @param stream the input stream to read from
     * @return the map of nodes
     * @throws IOException a read error of the input stream
     */
    public static MapOfNodes loadNodes(final InputStream stream)
            throws IOException {
        MapOfNodes result = new MapOfNodes();
        // open file and read properties
        Properties servicesProperties = new Properties();
        servicesProperties.load(stream);
        // go throught the properties and add nodes into the map
        for (Map.Entry<Object, Object> pair : servicesProperties.entrySet()) {
            String[] tokens = pair.getKey().toString().split("\\.", 2);
            // find a related node if exists, create a new otherwise
            Node node = (result.containsKey(tokens[0]))
                    ? result.get(tokens[0]) : new Node(tokens[0]);
            // fill the node
            if (tokens.length == 1) {
                node.self.set(pair.getValue().toString());
            } else if (tokens[1].equalsIgnoreCase("scan")) {
                node.scan.set(pair.getValue().toString());
            }
            // put the node into the map
            result.put(tokens[0], node);
        }
        return result;
    }

    /**
     * Save the map of nodes into a file.
     * @param filename the file to save into
     * @throws IOException a write error to the file
     */
    public void saveNodes(final String filename)
            throws IOException {
        OutputStream stream = new FileOutputStream(filename);
        try {
            saveNodes(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Save the map of nodes into an output stream.
     * @param stream the output stream to save into
     */
    public void saveNodes(final OutputStream stream) {
        PrintWriter printWriter = new PrintWriter(stream);
        for (Node node : this.values()) {
            printWriter.println(node.toString());
        }
    }
}
