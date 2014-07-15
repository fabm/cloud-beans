package pt.gapiap.cloud.endpoints;

import com.google.api.server.spi.config.ApiTransformer;

@ApiTransformer(CEReturnTransformer.class)
public interface CEReturn {

    /**
     * get cloud endpoint response
     *
     * @return
     * @throws CEError
     */
    Object getCEResponse() throws CEError;
}