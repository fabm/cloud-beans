package pt.gapiap.cloud.endpoints.errors;

import java.util.Map;

public class Failure {
    private String name;
    private Map<String, Object> vars;

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
