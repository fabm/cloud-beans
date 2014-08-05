package pt.gapiap.cloud.maps;

import java.util.HashMap;

public class ApiMap extends HashMap<String, ApiObject> implements ApiObject {

    private String name;
    private ApiValidator apiValidator;


    public void add(String method) {

    }


    @Override
    public Type getType() {
        return Type.METHOD;
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resolveMethod(String method) {

    }

    public void setApiValidator(ApiValidator apiValidator) {
        this.apiValidator = apiValidator;
    }
}
