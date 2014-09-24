package pt.gapiap.cloud.endpoints.errors;


import java.util.Map;

public interface ErrorTemplate {

  public String jsonTemplate();

  public Map<String,?> getJsonArguments();

  public String render(Object[] vars);

  public Type getType();

  static enum Type {
    SIMPLE, PARAMETRIZED;
  }
}