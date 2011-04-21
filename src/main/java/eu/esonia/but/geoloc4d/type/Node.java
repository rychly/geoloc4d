package eu.esonia.but.geoloc4d.type;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Node of a network with geolocation ability.
 * @author rychly
 */
public class Node {

    /**
     * Data about the node (itself).
     */
    public NodeData self;
    /**
     * Data about neighbouring nodes (from scan).
     */
    public MapOfNodes scan;

    public Node(final String representation) {
        this.self = new NodeData(representation);
        this.scan = new MapOfNodes();
    }

    public Node(final String id, final String representation) {
        this.self = new NodeData(id, representation);
        this.scan = new MapOfNodes();
    }

    @Override
    public String toString() {
        String id = self.getID();
        return id + "=" + self.toString().substring(id.length() + 1) + "\n"
                + id + ".scan=" + scan.toString();
    }

    /**
     * Loads a list of nodes from a file.
     * @param filename the file to read from
     * @throws FileNotFoundException the file not found
     * @throws IOException a read error of the file
     * @return the list of nodes
     */
    public static Map<String, Node> loadNodes(final String filename)
            throws FileNotFoundException, IOException {
        InputStream stream = new FileInputStream(filename);
        try {
            return loadNodes(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Loads a list of nodes from an input stream.
     * @param stream the input stream to read from
     * @return the list of nodes
     * @throws IOException a read error of the input stream
     */
    public static Map<String, Node> loadNodes(final InputStream stream)
            throws IOException {
        Map<String, Node> result = new HashMap<String, Node>();
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
     * Save a list of nodes into a file.
     * @param filename the file to save into
     * @param listOfNodes the list of nodes
     * @throws IOException a write error to the file
     */
    public static void saveNodes(final String filename, final Map<String, Node> listOfNodes)
            throws IOException {
        OutputStream stream = new FileOutputStream(filename);
        try {
            saveNodes(stream, listOfNodes);
        } finally {
            stream.close();
        }
    }

    /**
     * Save a list of nodes into an output stream.
     * @param stream the output stream to save into
     * @param listOfNodes the list of nodes
     */
    public static void saveNodes(final OutputStream stream, final Map<String, Node> listOfNodes) {
        PrintWriter printWriter = new PrintWriter(stream);
        for (Node node : listOfNodes.values()) {
            printWriter.println(node.toString());
        }
    }
}
