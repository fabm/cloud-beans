package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ErrorManager {
  private Map<Integer, ErrorLocale> globalErrors;
  private Map<Integer, ErrorLocale> clientErrors;
  private Map<Integer, ErrorLocale> errors;

  protected ErrorManager() {
    init();
  }

  public abstract List<? extends ErrorArea> getErrorAreas();

  protected void init() {
    globalErrors = Maps.newHashMap();
    clientErrors = Maps.newHashMap();
    ImmutableMap.Builder<Integer, ErrorLocale> builder = ImmutableMap.builder();

    for (ErrorArea errorArea : getErrorAreas()) {
      Set<Map.Entry<Integer, ErrorLocale>> errorLocals = errorArea.initLocaleErrors();
      for (Map.Entry<Integer, ErrorLocale> entry : errorLocals) {
        addError(entry.getKey(), entry.getValue());
        builder.put(entry.getKey(), entry.getValue());
      }
    }
    errors = builder.build();
    globalErrors = ImmutableMap.copyOf(globalErrors);
    clientErrors = ImmutableMap.copyOf(clientErrors);
  }

  private void addError(Integer key, ErrorLocale errorLocale) {
    if (globalErrors.containsKey(key) || clientErrors.containsKey(key)) {
      throw new RuntimeException("Colision with globalErrors with key " + key);
    }
    if (errorLocale.isClientError()) {
      clientErrors.put(key, errorLocale);
    } else {
      globalErrors.put(key, errorLocale);
    }
  }

  public ErrorTemplate getFailTemplate(int code, String language) {
    ErrorLocale errorLocale = errors.get(code);
    checkNotNull(errorLocale, "expected an error object");
    ErrorTemplate errorTemplate = errorLocale.getError(language);
    checkNotNull(errorLocale, "expected an error template");
    return errorTemplate;
  }

  public CEError create(int index, String language, Object... vars) {
    ErrorTemplate errorTemplate = getFailTemplate(index, language);
    Failure failure = new Failure(index , errorTemplate, vars);
    return new CEError(failure);
  }

  public Map<Integer, ErrorLocale> getClientErrors() {
    return clientErrors;
  }
}
