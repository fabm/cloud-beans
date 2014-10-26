package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ErrorManager {
  private Map<Integer, ErrorLocale> globalErrors;
  private Map<Integer, ErrorLocale> clientErrors;
  private Map<Integer, ErrorLocale> errors;

  public ErrorManager(List<? extends ErrorArea> areas) {
    init(areas);
  }

  protected void init(List<? extends ErrorArea> areas) {
    globalErrors = Maps.newHashMap();
    clientErrors = Maps.newHashMap();
    ImmutableMap.Builder<Integer, ErrorLocale> builder = ImmutableMap.builder();

    for (ErrorArea errorArea : areas) {
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

  public Map<Integer, ErrorLocale> getClientErrors() {
    return clientErrors;
  }

  public Map<Integer, ErrorLocale> getErrors() {
    return errors;
  }
}
