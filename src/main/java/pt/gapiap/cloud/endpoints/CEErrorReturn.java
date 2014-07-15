package pt.gapiap.cloud.endpoints;

public interface CEErrorReturn {
    public static final String NOT_IMPLEMENTED = "Not implemented yet";

    String getContext();

    int getCode();

    String getMsg();
}

