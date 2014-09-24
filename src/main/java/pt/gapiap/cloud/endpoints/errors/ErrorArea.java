package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ErrorArea {
  private int[] errorClientIndexes;

  abstract protected List<? extends ErrorContent> getErrorContents();

  abstract protected Map<String, ?> getArgumentsMap(int index);

  private boolean isClientError(int index) {
    if (errorClientIndexes == null) {
      errorClientIndexes = getClientErrorIndexes();
    }

    if (errorClientIndexes == null) {
      return false;
    }


    for (int i = 0; i < errorClientIndexes.length; i++) {
      if (errorClientIndexes[i] == index) {
        return true;
      }
    }
    return false;
  }


  Set<Map.Entry<Integer, ErrorLocale>> initLocaleErrors() {
    errorClientIndexes = getClientErrorIndexes();
    List<? extends ErrorContent> errorContents = getErrorContents();

    checkNotNull("getErrorContents must be not null");

    Map<Integer, ErrorLocale> errorLocaleMap = Maps.newHashMap();

    for (ErrorContent errorContent : errorContents) {
      ErrorLocale errorLocale;
      for (Map.Entry<Integer, ErrorTemplate> templateEntry : errorContent) {
        errorLocale = errorLocaleMap.get(templateEntry.getKey());
        if (errorLocale == null) {
          errorLocale = new ErrorLocale();
          errorLocale.setClient(isClientError(templateEntry.getKey()));
          errorLocale.setArgs(errorContent.getArgs());
          errorLocaleMap.put(templateEntry.getKey(), errorLocale);
        }
        errorLocale.setError(errorContent.getLanguage(), templateEntry.getValue());
      }

    }

    Integer languageSize = null;
    for (Map.Entry<Integer, ErrorLocale> entry : errorLocaleMap.entrySet()) {
      if (languageSize == null) {
        languageSize = entry.getValue().getLanguageSize();
      }
      if (languageSize != entry.getValue().getLanguageSize()) {
        throw new RuntimeException("The number of errors must be equal in all languages and vice-versa");
      }
    }

    return errorLocaleMap.entrySet();
  }

  protected abstract int[] getClientErrorIndexes();

}
