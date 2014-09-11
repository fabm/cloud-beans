package pt.gapiap.cloud.endpoints.errors;

import java.text.MessageFormat;

public class ParamterizedErrorTemplate implements ErrorTemplate {
    private String message;

    public ParamterizedErrorTemplate(String message) {
        this.message = message;
    }

    @Override
    public String jsonTemplate() {
        MessageFormat messageFormat = new MessageFormat(message);
        int count = messageFormat.getFormats().length;
        String divisor = "'],['";
        String[] divisors = new String[count];
        for (int i = 0; i < count; i++) {
            divisors[i] = divisor;
        }
        return "['" + messageFormat.format(divisors) + "']";
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
