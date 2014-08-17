package pt.gapiap.cloud.endpoints.errors;


import pt.gapiap.cloud.endpoints.CEErrorIdentifier;

public enum GeneralErrorIdentifier implements CEErrorIdentifier {
    NOT_AUTHORIZED(1),
    NOT_AUTHENTICATED(2),
    UNEXPECTED(3);

    private int code;

    GeneralErrorIdentifier(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getContext() {
        return "General";
    }

}
