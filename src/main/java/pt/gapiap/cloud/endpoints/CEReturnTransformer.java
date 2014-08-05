package pt.gapiap.cloud.endpoints;

import com.google.api.server.spi.config.Transformer;
import com.google.api.server.spi.response.UnauthorizedException;

public class CEReturnTransformer implements Transformer<CEReturn, Object> {

    @Override
    public Object transformTo(CEReturn ceReturn) {
        try {
            return ceReturn.getCEResponse();
        } catch (CEError CEError) {
            return CEError.getMap();
        } catch (UnauthorizedException e) {
            return e;
        }
    }

    @Override
    public CEReturn transformFrom(Object object) {
        return null;
    }

}