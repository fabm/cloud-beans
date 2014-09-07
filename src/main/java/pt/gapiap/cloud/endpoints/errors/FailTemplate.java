package pt.gapiap.cloud.endpoints.errors;


public interface FailTemplate {
    public boolean isClientError();

    public String jsonTemplate();

    public String render(Object[] vars);
}