package eu.esonia.but.geoloc4d;

import java.util.HashMap;
import java.util.Map;

/**
 * Scan-list as a hash-map.
 * @author rychly
 */
public final class MapOfNodes extends HashMap<String, Integer> {

    MapOfNodes() {
        super();
    }

    MapOfNodes(String scanList) {
        this.set(scanList);
    }

    /**
     * Add content of scan list from another.
     * @param scanList source scan list
     */
    public void set(MapOfNodes scanList) {
        this.putAll(scanList);
    }

    /**
     * Set scan list from string (e.g. "MapOfNodes:NodeA(123);NodeB(345);NodeC(678)").
     * @param scanList string with scan list
     */
    public void set(String scanList) {
        String[] tokens = scanList.split(" *[:();] *");
        for (int position = 1; position < tokens.length; position += 3) {
            this.put(tokens[position], Integer.parseInt(tokens[position + 1]));
        }
    }

    @Override
    public String toString() {
        String result = MapOfNodes.class.getSimpleName() + ":";
        for (Map.Entry<String, Integer> pair : this.entrySet()) {
            result = result.concat(pair.getKey() + "(" + pair.getValue() + ");");
        }
        return result.substring(0, result.length() - 1);
    }
}
