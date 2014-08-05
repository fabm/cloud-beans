package pt.gapiap.proccess.wrappers;


public class ApiField {
    Iterable<ValidationRule> validationRuleIterable;
    private String name;

    public Iterable<ValidationRule> getValidationRuleIterable() {
        return validationRuleIterable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
