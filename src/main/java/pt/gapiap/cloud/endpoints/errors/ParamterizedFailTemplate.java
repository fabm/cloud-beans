package pt.gapiap.cloud.endpoints.errors;

import java.text.MessageFormat;

public class ParamterizedFailTemplate implements FailTemplate {
    private String message;
    private boolean client;

    public ParamterizedFailTemplate(String message,boolean client) {
        this.message = message;
        this.client = client;
    }
    public ParamterizedFailTemplate(String message) {
        this.message = message;
        this.client = false;
    }

    @Override
    public boolean isClientError() {
        return client;
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
}
