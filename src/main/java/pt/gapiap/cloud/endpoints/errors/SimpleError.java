package pt.gapiap.cloud.endpoints.errors;

import pt.gapiap.cloud.endpoints.CEErrorIdentifier;

import java.util.Map;

public class SimpleError extends CEError {
    private Map<String, Object> vars;

    public SimpleError(CEErrorIdentifier ceErrorIdentifier, Map<String, Object> vars) {
        super(ceErrorIdentifier);
        this.vars = vars;
    }

    public Map<String, Object> getVars() {
        return vars;
    }
}
