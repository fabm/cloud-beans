package pt.gapiap.cloud.maps;

import java.util.HashMap;

public class ApiMap extends HashMap<String, ApiObject> implements ApiObject {

    private String name;
    private ApiValidationMap apiValidationMap;


    public void add(String method) {

    }


    @Override
    public Type getType() {
        return Type.METHOD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resolveMethod(String method) {

    }

    public void setApiValidationMap(ApiValidationMap apiValidationMap) {
        this.apiValidationMap = apiValidationMap;
    }
}
