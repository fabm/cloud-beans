package pt.gapiap.cloud.endpoints;

import java.util.Map;

public interface ErrorField {
    CEErrorIdentifier getCEErrorReturn();
    Map<String,String> getVarMap();
}
