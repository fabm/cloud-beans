package pt.gapiap.cloud.endpoints.errors;

import java.util.Map;

public class SimpleErrorTemplate implements ErrorTemplate {

    private String error;
  private Map<String, ?> jsonArguments;

  public SimpleErrorTemplate(String error,Map<String,?> jsonArguments) {
    this.error = error;
    this.jsonArguments = jsonArguments;
  }
  public SimpleErrorTemplate(String error) {
    this.error = error;
  }

    @Override
    public String jsonTemplate() {
        return "['"+error+"']";
    }

  @Override
  public Map<String, ?> getJsonArguments() {
    return jsonArguments;
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
