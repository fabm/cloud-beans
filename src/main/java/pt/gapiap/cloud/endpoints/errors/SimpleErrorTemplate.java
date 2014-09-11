package pt.gapiap.cloud.endpoints.errors;

public class SimpleErrorTemplate implements ErrorTemplate {

    private String error;

    public SimpleErrorTemplate(String error) {
        this.error = error;
    }

    @Override
    public String jsonTemplate() {
        return "['"+error+"']";
    }

    @Override
    public String render(Object[] vars) {
        return error;
    }

  @Override
  public Type getType() {
    return Type.SIMPLE;
  }
}
