package pt.gapiap.cloud.endpoints.errors;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ErrorArea {
  private ImmutableList<ErrorLocale> localeErrors;
  private int startIndex;

  @Inject
  public ErrorArea(ErrorManager errorManager) {
    initLocalErrors();
    startIndex = errorManager.errorsRegister(localeErrors);
  }

  private Supplier<ErrorLocale[]> getSupplier(final int length) {
    return new Supplier<ErrorLocale[]>() {
      @Override
      public ErrorLocale[] get() {
        return new ErrorLocale[length];
      }
    };
  }

  protected void initLocalErrors() {
    Optional<int[]> optClientErrorsIndexes = Optional.fromNullable(getClientErrorIndexes());
    List<? extends ErrorContent> errorContents = getErrorContents();
    checkNotNull("getErrorContents must be not null");
    ErrorLocale[] arrLocales = null;

    Optional<Integer> opt = Optional.absent();

    for (ErrorContent errorContent : errorContents) {
      final ErrorTemplate[] errorTemplates = errorContent.getErrorTemplates();

      if (opt.isPresent()) {
        if (opt.get().equals(errorTemplates.length)) {
          arrLocales = Optional.fromNullable(arrLocales).or(getSupplier(errorTemplates.length));
        } else {
          throw new RuntimeException("The number of errors must be equal in all languages");
        }
      }
      for (int i = 0; i < errorTemplates.length; i++) {
        arrLocales[i].setError(errorContent.getLanguage(), errorTemplates[i]);
      }
      opt = Optional.of(errorTemplates.length);
    }
    if (optClientErrorsIndexes.isPresent()) {
      defineClientErrors(optClientErrorsIndexes.get(), arrLocales);
    }

    localeErrors = ImmutableList.copyOf(arrLocales);
  }

  private void defineClientErrors(int[] clientErrorsIndexes, ErrorLocale[] arrLocales) {
    for (int i = 0; i < clientErrorsIndexes.length; i++) {
      int ic = clientErrorsIndexes[i];
      String msgOutOfBounds = String.format("The error with the index %d is not content in array", ic);
      checkElementIndex(ic, arrLocales.length, msgOutOfBounds);
      arrLocales[ic].setClient(true);
    }
  }

  abstract protected List<? extends ErrorContent> getErrorContents();


  public List<ErrorLocale> getLocaleErrors() {
    return localeErrors;
  }


  public ErrorTemplate getFailTemplate(int code, String language) {
    ErrorLocale errorLocale = localeErrors.get(code);
    checkNotNull(errorLocale, "expected an error object");
    ErrorTemplate errorTemplate = errorLocale.getError(language);
    checkNotNull(errorLocale, "expected an error template");
    return errorTemplate;
  }

  public CEError create(int index, String language, Object... vars) {
    ErrorTemplate errorTemplate = getFailTemplate(index, language);
    Failure failure = new Failure(this.startIndex + index, errorTemplate, vars);
    return new CEError(failure);
  }

  protected abstract int[] getClientErrorIndexes();
}
