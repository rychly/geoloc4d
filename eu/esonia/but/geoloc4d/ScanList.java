package eu.esonia.but.geoloc4d;

import java.util.HashMap;
import java.util.Map;

/**
 * Scan-list as a hash-map.
 * @author rychly
 */
public class ScanList extends HashMap<String, Integer> {

    ScanList() {
        super();
    }

    ScanList(String scanList) {
        this.set(scanList);
    }

    public void set(ScanList scanList) {
        this.putAll(scanList);
    }

    public void set(String scanList) {
        String[] tokens = scanList.split(" *[:();] *");
        for (int position = 1; position < tokens.length; position = position + 3) {
            this.put(tokens[position], Integer.parseInt(tokens[position+1]));
        }
    }

    @Override
    public String toString() {
        String result = ScanList.class.getSimpleName() + ":";
        for (Map.Entry<String, Integer> pair : this.entrySet()) {
            result.concat(pair.getKey() + "(" + pair.getValue() + ");");
        }
        return result.substring(0, result.length()-1);
    }
}
