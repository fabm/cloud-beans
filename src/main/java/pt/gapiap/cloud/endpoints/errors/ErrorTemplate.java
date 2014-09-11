package pt.gapiap.cloud.endpoints.errors;


public interface ErrorTemplate {

  public String jsonTemplate();

  public String render(Object[] vars);

  public Type getType();

  static enum Type {
    SIMPLE, PARAMETRIZED;
  }
}