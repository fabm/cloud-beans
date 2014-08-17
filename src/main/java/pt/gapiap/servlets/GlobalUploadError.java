package pt.gapiap.servlets;

import pt.gapiap.cloud.endpoints.CEErrorIdentifier;

public enum GlobalUploadError implements CEErrorIdentifier {
    NO_ACTION_PARAMETER,
    NO_UPLOAD_ACTION_REGISTERED;

    private static String[] messages;

    //TODO passar as mensagens para um json
    static {
        messages = new String[GlobalUploadError.values().length];
        messages[NO_ACTION_PARAMETER.ordinal()] = "There is no parameter fo the action";
        messages[NO_UPLOAD_ACTION_REGISTERED.ordinal()] = "There is no upload registered for the action '%s'";
    }

    @Override
    public String getContext() {
        return "upload";
    }

    @Override
    public int getCode() {
        return this.ordinal();
    }


}
