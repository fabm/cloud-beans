package pt.gapiap.proccess.validation.defaultValidator;

import pt.gapiap.cloud.endpoints.CEErrorIdentifier;

public enum DefaultValidatorIdentifier implements CEErrorIdentifier {
    NOT_NULL(1),
    EMAIL(2),
    SIZE(3),
    MIN(4),
    MAX(5);

    private int code;

    DefaultValidatorIdentifier(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getContext() {
        return "DefaultValidation";
    }
}
