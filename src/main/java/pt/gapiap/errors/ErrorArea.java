package pt.gapiap.errors;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.cloud.endpoints.errors.Failure;
import pt.gapiap.cloud.endpoints.errors.ParamterizedFailTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleFailTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ErrorArea {
  private int startIndex;
  private Map<String, FailTemplate[]> errorTable;

  @Inject
  public ErrorArea(ErrorManager errorManager) {
    errorTable = new HashMap<>();
    init();
    afterInit(errorManager);
  }

  public abstract int getSize();

  private void afterInit(ErrorManager errorManager) {
    errorManager.registerRange(this);
  }

  protected abstract void init();

  public Set<Map.Entry<String, FailTemplate[]>> getFailTemplates() {
    return errorTable.entrySet();
  }

  private Optional<FailTemplate[]> getOptArray(String language) {
    Optional<FailTemplate[]> optErrors = Optional.of(errorTable.get(language));
    optErrors.or(new Supplier<FailTemplate[]>() {
      @Override
      public FailTemplate[] get() {
        return new FailTemplate[getSize()];
      }
    });
    return optErrors;
  }

  public FailTemplate getFailTemplate(int code, String language) {
    FailTemplate[] array = errorTable.get(language);
    checkNotNull(array, "expected a array from errorTable");
    checkElementIndex(code, array.length, "expected a FailedTemplate from array");
    FailTemplate failTemplate = array[code];
    return failTemplate;
  }

  private void validateIndex(int index) {
    if (index < 1 || index > getSize()) {
      throw new IllegalArgumentException("index of error must be between 0 and " + getSize());
    }
  }

  protected void setSimpleError(int index, String language, String message, boolean client) {
    validateIndex(index);
    Optional<FailTemplate[]> opList = getOptArray(language);

    opList.get()[index] = new SimpleFailTemplate(message, client);

    if (!opList.isPresent()) {
      errorTable.put(language, opList.get());
    }
  }

  protected void setParametrizedError(int index, String language, String message, boolean client) {
    validateIndex(index);
    Optional<FailTemplate[]> opList = getOptArray(language);

    opList.get()[index] = new ParamterizedFailTemplate(message, client);

    if (!opList.isPresent()) {
      errorTable.put(language, opList.get());
    }
  }

  protected void setArrayLanguage(String language, ErrorContents errorContents) {
    Optional<FailTemplate[]> optArray = Optional.fromNullable(errorTable.get(language));
    if (optArray.isPresent()) {
      throw new RuntimeException("language already exists in the map");
    } else {
      errorTable.put(language, errorContents.getFailTemplates());
    }
  }

  void setStartIndex(int index) {
    this.startIndex = index;
  }

  public CEError create(int index, String language, Object... vars) {
    FailTemplate failTemplate = getFailTemplate(index, language);
    Failure failure = new Failure(this.startIndex + index, failTemplate, vars);
    return new CEError(failure);
  }
}
