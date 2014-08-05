package pt.gapiap.cloud.endpoints;


public enum GlobalError implements CEErrorReturn {
    REQUIRED,
    EMAIL,
    NOT_AUTHENTICATED,
    NOT_AUTHORIZED,
    NO_NAME;

    private static final String[] messages;

    static {
        messages = new String[GlobalError.values().length];
        messages[REQUIRED.ordinal()] = "The field %s is required";
        messages[EMAIL.ordinal()] = "The field %s is not formated correctly";
        messages[NOT_AUTHENTICATED.ordinal()] = "You must be authenticated";
        messages[NOT_AUTHORIZED.ordinal()] = "You are not authorized to access '%s' area";
        messages[NO_NAME.ordinal()] = "Misses the user name";
    }

    @Override
    public String getContext() {
        return "global";
    }

    @Override
    public int getCode() {
        return this.ordinal();
    }

    @Override
    public String getMsg() {
        return messages[this.ordinal()];
    }
}
