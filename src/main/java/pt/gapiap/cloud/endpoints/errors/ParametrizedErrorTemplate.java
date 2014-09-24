package pt.gapiap.cloud.endpoints.errors;

import java.text.MessageFormat;
import java.util.Map;

public class ParametrizedErrorTemplate implements ErrorTemplate {
  private String message;
  private Map<String, ?> jsonArguments;

  public ParametrizedErrorTemplate(String message, Map<String, ?> jsonArguments) {
    this.message = message;
    this.jsonArguments = jsonArguments;
  }

  public ParametrizedErrorTemplate(String message) {
    this.message = message;
  }

  @Override
  public String jsonTemplate() {
    MessageFormat messageFormat = new MessageFormat(message);
    int count = messageFormat.getFormats().length;
    String divisor = "\",\"";
    String[] divisors = new String[count];
    for (int i = 0; i < count; i++) {
      divisors[i] = divisor;
    }
    return "[\"" + messageFormat.format(divisors) + "\"]";
  }

  @Override
  public Map<String, ?> getJsonArguments() {
    return jsonArguments;
  }

  @Override
  public String render(Object[] args) {
    MessageFormat messageFormat = new MessageFormat(message);
    return messageFormat.format(args);
  }

  @Override
  public Type getType() {
    return Type.PARAMETRIZED;
  }
}
