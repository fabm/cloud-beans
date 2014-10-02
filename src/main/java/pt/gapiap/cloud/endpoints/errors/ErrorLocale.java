package pt.gapiap.cloud.endpoints.errors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ErrorLocale implements Iterable<Map.Entry<String, ErrorTemplate>> {
  private Map<String, ErrorTemplate> errorTemplateMap;
  private Map<String, ?> args;
  private boolean clientError;

  public ErrorLocale() {
    this.errorTemplateMap = new HashMap<>();
  }

  ErrorTemplate setError(String language, ErrorTemplate errorTemplate) {
    return this.errorTemplateMap.put(language, errorTemplate);
  }

  public ErrorTemplate getError(String language) {
    return errorTemplateMap.get(language);
  }

  @Override
  public Iterator<Map.Entry<String, ErrorTemplate>> iterator() {
    return errorTemplateMap.entrySet().iterator();
  }

  public boolean isClientError() {
    return clientError;
  }

  public void setClient(boolean client) {
    this.clientError = client;
  }

  public Map<String, ?> getArgs() {
    return args;
  }

  public void setArgs(Map<String, ?> args) {
    this.args = args;
  }

  int getLanguageSize(){
    return errorTemplateMap.size();
  };
}
