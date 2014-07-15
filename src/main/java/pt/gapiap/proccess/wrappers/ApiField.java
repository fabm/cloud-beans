package pt.gapiap.proccess.wrappers;

import java.util.Map;

public class ApiField {
    private String name;
    private Map<String, Object> validationRules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValidationRules(Map<String, Object> validationRules) {
        this.validationRules = validationRules;
    }

    public Map<String, Object> getValidationRules() {
        return validationRules;
    }
}
