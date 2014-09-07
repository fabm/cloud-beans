package pt.gapiap.cloud.endpoints.errors;

public class SimpleFailTemplate implements FailTemplate {

    private String error;
    private boolean client;

    public SimpleFailTemplate(String error,boolean client) {
        this.error = error;
        this.client = client;
    }
    public SimpleFailTemplate(String error) {
        this.error = error;
        this.client = false;
    }

    @Override
    public boolean isClientError() {
        return client;
    }

    @Override
    public String jsonTemplate() {
        return "['"+error+"']";
    }

    @Override
    public String render(Object[] vars) {
        return error;
    }
}
