package pt.gapiap.cloud.endpoints.errors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;

import static com.google.common.base.Preconditions.checkNotNull;

@RequestScoped
public class FailureManager {
  @Inject @Named("language")
  private String language;

  @Inject
  private ErrorManager errorManager;

  public ErrorTemplate getFailTemplate(int code) {
    ErrorLocale errorLocale = errorManager.getErrors().get(code);
    checkNotNull(errorLocale, "expected an error object");
    ErrorTemplate errorTemplate = errorLocale.getError(language);
    checkNotNull(errorLocale, "expected an error template");
    return errorTemplate;
  }

  public Failure createFailure(int index, Object... vars){
    ErrorTemplate errorTemplate = getFailTemplate(index);
    Failure failure = new Failure(index , errorTemplate, vars);
    return failure;
  }

  public CEError createError(int index, Object... vars) {
    ErrorTemplate errorTemplate = getFailTemplate(index);
    Failure failure = new Failure(index , errorTemplate, vars);
    return new CEError(failure);
  }
}
