package eu.esonia.but.geoloc4d;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Node of a network with geolocation ability.
 * @author rychly
 */
public class Node {

    public NodeData self;
    public MapOfNodes scan;

    Node(final String representation) {
        this.self = new NodeData(representation);
        this.scan = new MapOfNodes();
    }

    Node(final String id, final String representation) {
        this.self = new NodeData(id, representation);
        this.scan = new MapOfNodes();
    }

    /**
     * Reads a list of nodes from a file.
     * @param filename the file to read from
     * @return the list of nodes
     * @throws FileNotFoundException the file not found
     * @throws IOException a read error of the file
     */
    public static Map<String, Node> readNodes(final String filename)
            throws FileNotFoundException, IOException {
        InputStream stream = new FileInputStream(filename);
        try {
            return readNodes(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Reads a list of nodes from an input stream.
     * @param stream the input stream to read from
     * @return the list of nodes
     * @throws IOException a read error of the input stream
     */
    public static Map<String, Node> readNodes(final InputStream stream)
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
}
