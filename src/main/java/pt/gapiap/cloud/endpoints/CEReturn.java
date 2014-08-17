package pt.gapiap.cloud.endpoints;

import com.google.api.server.spi.config.ApiTransformer;
import pt.gapiap.cloud.endpoints.errors.CEError;

@ApiTransformer(CEReturnTransformer.class)
public interface CEReturn {

    /**
     * get cloud endpoint response
     *
     * @return
     * @throws pt.gapiap.cloud.endpoints.errors.CEError
     */
    Object getCEResponse() throws CEError;
}