package eu.esonia.but.geoloc4d;

import org.ws4d.java.communication.TimeoutException;
import org.ws4d.java.schema.Element;
import org.ws4d.java.schema.SchemaUtil;
import org.ws4d.java.service.DefaultService;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.types.QName;
import org.ws4d.java.util.ParameterUtil;

/**
 * Asset Service.
 * @author rychly
 */
public class AssetService extends DefaultService {

    // Namespace for services
    public static final String NAMESPACE = "http://www.fit.vutbr.cz/~rychly/geoloc4d";
    // Location coordinates
    private Vector3D location;
    // Scan of neighbouring nodes
    private ScanList neighboursScan;

    AssetService(int id) {
	// if (id == -1) then do not use properties for configuration
        super(id);
        // Add operations
        this.addOperation(new ScanNeighbours(this));
        this.addOperation(new GetLocation(this));
        this.addOperation(new SetLocation(this));
        // Init properties
        this.location = new Vector3D();
        this.neighboursScan = new ScanList();
    }

    public void setLocation(int x, int y, int z) {
        this.location.set(x, y, z);
    }

    public void setLocation(Vector3D location) {
        this.location.set(location);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public Vector3D getLocation() {
        return new Vector3D(location);
    }

    public void setNeighboursScan(ScanList neighboursScan) {
        this.neighboursScan.set(neighboursScan);
    }

    public void setNeighboursScan(String neighboursScan) {
        this.neighboursScan.set(neighboursScan);
    }

    public ScanList getNeighboursScan() {
        return this.neighboursScan;
    }

    class ScanNeighbours extends Operation {

        AssetService assetService;

        ScanNeighbours(AssetService assetService) {
            this();
            this.assetService = assetService;
        }

        ScanNeighbours() {
            super(ScanNeighbours.class.getSimpleName(), new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE));
            // This defines the format of the output and input.
            Element out = new Element(
                    new QName(Vector3D.class.getSimpleName(), AssetService.NAMESPACE),
                    SchemaUtil.getSchemaType(SchemaUtil.TYPE_STRING));
            this.setOutput(out);
        }

        // If the method is invoked by the client this method will be called. The returned ParameterValue is the answer. It will be sent to the client.
        public ParameterValue invoke(ParameterValue parameterValues) throws InvocationException, TimeoutException {
            ParameterValue returnValue = createOutputValue();
            ParameterUtil.setString(returnValue, null, this.assetService.getNeighboursScan().toString());
            return returnValue;
        }
    }

    class GetLocation extends Operation {

        AssetService assetService;

        GetLocation(AssetService assetService) {
            this();
            this.assetService = assetService;
        }

        GetLocation() {
            super(GetLocation.class.getSimpleName(), new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE));
            // This defines the format of the output and input.
            Element out = new Element(
                    new QName(Vector3D.class.getSimpleName(), AssetService.NAMESPACE),
                    SchemaUtil.getSchemaType(SchemaUtil.TYPE_STRING));
            this.setOutput(out);
        }

        // If the method is invoked by the client this method will be called. The returned ParameterValue is the answer. It will be sent to the client.
        public ParameterValue invoke(ParameterValue parameterValues) throws InvocationException, TimeoutException {
            ParameterValue returnValue = createOutputValue();
            ParameterUtil.setString(returnValue, null, this.assetService.getLocation().toString());
            return returnValue;
        }
    }

    class SetLocation extends Operation {

        AssetService assetService;

        SetLocation(AssetService assetService) {
            this();
            this.assetService = assetService;
        }

        SetLocation() {
            super(SetLocation.class.getSimpleName(), new QName(AssetService.class.getSimpleName(), AssetService.NAMESPACE));
            // This defines the format of the output and input.
            Element in = new Element(
                    new QName(Vector3D.class.getSimpleName(), AssetService.NAMESPACE),
                    SchemaUtil.getSchemaType(SchemaUtil.TYPE_STRING));
            this.setInput(in);
        }

        // If the method is invoked by the client this method will be called. The returned ParameterValue is the answer. It will be sent to the client.
        public ParameterValue invoke(ParameterValue parameterValues) throws InvocationException, TimeoutException {
            this.assetService.setLocation(ParameterUtil.getString(parameterValues, null));
            return null;
        }
    }
}
