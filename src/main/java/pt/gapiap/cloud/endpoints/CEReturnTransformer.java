package pt.gapiap.cloud.endpoints;

import com.google.api.server.spi.config.Transformer;
import com.google.gson.GsonBuilder;
import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.GeneralErrorIdentifier;
import pt.gapiap.cloud.endpoints.errors.SimpleError;
import pt.gapiap.cloud.endpoints.serializers.SimpleErrorSerializer;

public class CEReturnTransformer implements Transformer<CEReturn, Object> {


    @Override
    public Object transformTo(CEReturn ceReturn) {
            GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SimpleError.class,new SimpleErrorSerializer());
        try {
            return ceReturn.getCEResponse();
        } catch (CEError ceError) {
            return gsonBuilder.create().toJson(ceError);
        } catch (RuntimeException e) {
            return GeneralErrorIdentifier.UNEXPECTED;
        }
    }

    @Override
    public CEReturn transformFrom(Object object) {
        return null;
    }

}