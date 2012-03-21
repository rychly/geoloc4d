package eu.esonia.but.geoloc4d.type;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * Basic data of a node.
 *
 * @author rychly
 */
public class NodeData implements JSONString {

    /**
     * Identificator of the node, its name or URI (required).
     */
    private String id;
    /**
     * Absolute location of the node (optional).
     */
    private Vector3D locationAbsolute;
    /**
     * IPv4 or IPv6 address of the node (optional). For example, it can be
     * 1.2.3.4 or [2001:db8:bbbb:abcd:280:e102:11:e080].
     */
    private String ip;

    protected NodeData() {
        // empty constructor for an initiation from subclasses
    }

    /**
     * Constructor of a node from its string representation in JSON.
     *
     * @param representation string representation of the node properties
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the string representation in JSON
     */
    public NodeData(final String representation) throws NodeParsingException, JSONException {
        this(new JSONObject(representation));
    }

    /**
     * Constructor of a node from its representation as JSONObject.
     *
     * @param representation representation of the node properties in JSONObject
     * @throws NodeParsingException fail to parse the string representation
     * @throws JSONException fail to parse the representation in JSON
     */
    public NodeData(final JSONObject representation) throws NodeParsingException, JSONException {
        // id
        if (!representation.has("id")) {
            throw new NodeParsingException("Unset 'id'!");
        }
        this.setID(representation.getString("id"));
        // ip
        if (representation.has("ip")) {
            this.setIP(representation.getString("ip"));
        }
        // locationAbsolute
        if (representation.has("locationAbsolute")) {
            this.setLocationAbsolute(new Vector3D(representation.getJSONArray("locationAbsolute")));
        }
    }

    /**
     * Copy constructor.
     *
     * @param source source to copy from
     */
    public NodeData(final NodeData source) {
        // String constructors are necessary in the copy constructor
        this.setID(new String(source.getID()));
        String string = source.getIP();
        if (string != null) {
            this.setIP(new String(string));
        }
        Vector3D vector = source.getLocationAbsolute();
        if (vector != null) {
            this.setLocationAbsolute(new Vector3D(vector));
        }
    }

    /**
     * Set identificator of the node (e.g. its name).
     *
     * @param id the identificator to set
     */
    protected final void setID(final String id) {
        this.id = id;
    }

    /**
     * Get identificator of the node (e.g. its name).
     *
     * @return identificator of the node
     */
    public final String getID() {
        return this.id;
    }

    /**
     * Set IPv4 or IPv6 address of the node.
     *
     * @param ip the IPv4 or IPv6 address
     */
    protected final void setIP(final String ip) {
        this.ip = ip;
    }

    /**
     * Get IPv4 or IPv6 address from ID (is URI) or "IP" attribute.
     *
     * @return the IPv4 or IPv6 address
     */
    public final String getIP() {
        // IP is defined in the attribute
        if (( this.ip != null ) && !this.ip.isEmpty()) {
            return this.ip;
        }
        // IP can be in ID, if ID is URI
        URI uri = this.getURI();
        if (uri != null) {
            return uri.getHost();
        } else {
            return null;
        }
    }

    /**
     * Get URI in the case ID is the URI, null otherwise. For example, ID can be
     * http://[2001:db8:bbbb:abcd:280:e102:11:e080]/myservice.
     *
     * @return the URI in the case ID is the URI, null otherwise
     */
    public final URI getURI() {
        String uriString = this.getID();
        if ( uriString.startsWith("http://") || uriString.startsWith("https://") ) {
            try {
                return new URI(uriString).normalize();
            }
            catch (URISyntaxException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject result = new JSONObject();
            result.put("id", this.getID());
            result.putOpt("ip", this.getIP());
            if (this.getLocationAbsolute() != null) {
                result.put("locationAbsolute", this.getLocationAbsolute().toJSONArray());
            }
            return result;
            // e.g. "{id:"SecondNode","locationAbsolute":[0,1000,0]}
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be a non-finite number!", ex);
        }
    }

    @Override
    public String toString() {
        try {
            return this.toJSONObject().toString(1);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Impossible, the value cannot be an invalid number!", ex);
        }
    }

    @Override
    public String toJSONString() {
        return this.toJSONObject().toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (!( object instanceof NeighbourProperties )) {
            return false;
        } else {
            NeighbourProperties neighbourProperties = (NeighbourProperties) object;
            return ( ( this.getID() == null && neighbourProperties.getID() == null ) || this.getID().equals(neighbourProperties.getID()) )
                    && ( ( this.getIP() == null && neighbourProperties.getIP() == null ) || this.getIP().equals(neighbourProperties.getIP()) )
                    && ( ( this.getLocationAbsolute() == null && neighbourProperties.getLocationAbsolute() == null ) || this.getLocationAbsolute().equals(neighbourProperties.getLocationAbsolute()) );
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getID());
        hash = 97 * hash + Objects.hashCode(this.getIP());
        hash = 97 * hash + Objects.hashCode(this.getLocationAbsolute());
        return hash;
    }

    /**
     * Check if the node has defined its absolute location.
     *
     * @return true iff the node has defined its absolute location
     */
    public final boolean isAbsolutelyLocalised() {
        return ( this.getLocationAbsolute() != null ) && this.getLocationAbsolute().isDefined();
    }

    /**
     * @return the locationAbsolute
     */
    public final Vector3D getLocationAbsolute() {
        return locationAbsolute;
    }

    /**
     * @param locationAbsolute the locationAbsolute to set
     */
    public final void setLocationAbsolute(Vector3D locationAbsolute) {
        this.locationAbsolute = locationAbsolute;
    }
}
