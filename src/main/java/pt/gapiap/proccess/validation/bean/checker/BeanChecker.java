package pt.gapiap.proccess.validation.bean.checker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.cloud.endpoints.errors.FailTemplate;
import pt.gapiap.errors.ErrorArea;
import pt.gapiap.proccess.annotations.Embedded;
import pt.gapiap.proccess.validation.LocaleFieldName;
import pt.gapiap.proccess.validation.LocaleFieldNameDefault;
import pt.gapiap.runtime.reflection.ReflectField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;


/**
 * <p>
 * Class to validate beans, mainly it's an engine witch uses a validator bean
 * like {@link pt.gapiap.proccess.validation.defaultValidator.DefaultValidator} to run the methods annotated with
 * {@link pt.gapiap.proccess.validation.ValidationMethod} each method will run in the proper priority order
 * and will be injected the context of the field
 * </p>
 * <p>
 * It's required to inject members of BeanChecker instance, with {@link com.google.inject.Injector#injectMembers(Object)} after its creation.
 * These procedure is to assignee the singleton {@link pt.gapiap.errors.ErrorArea}'s which should be present in the {@link com.google.inject.Module} of
 * Guice injection
 * </p>
 */
public class BeanChecker {
  private final String language;
  protected ValidatorCenter validatorCenter;
  protected Checker checker;

  @Inject
  private Injector injector;
  private LocaleFieldName localeFieldName;

  //todo completar javadoc
  /**
   *
   * @param checkAllErrors
   * @param language
   * @param localeFieldName
   */

  public BeanChecker(boolean checkAllErrors, String language, LocaleFieldName localeFieldName) {
    this.language = language;
    this.localeFieldName = localeFieldName;
    init(checkAllErrors);
  }

  public BeanChecker(boolean checkAllErrors) {
    this.language = "en";
    this.localeFieldName = new LocaleFieldNameDefault();
    init(checkAllErrors);
  }

  private void init(boolean checkAllErrors) {
    if (checkAllErrors) {
      checker = new CheckerAcomulator();
    } else {
      checker = new SimpleChecker();
    }
  }

  /**
   * @return the current validator
   */
  public ValidatorCenter getValidatorCenter() {
    return validatorCenter;
  }

  /**
   * Checks the validation of a bean
   *
   * @param object
   * @param classes
   * @param <T>     object type
   * @return
   * @throws BeanCheckerException
   */

  @SuppressWarnings("unchecked")
  public <T> T check(T object, Class<? extends T>... classes) {
    if (object != null && classes.length == 0) {
      classes = new Class[]{object.getClass()};
    }
    validatorCenter = injector.getInstance(ValidatorCenter.class);
    injector.injectMembers(validatorCenter);
    validatorCenter.init(object);

    for (Class<? extends T> clazz : classes) {
      check(object, clazz);
    }
    return object;
  }

  private void check(Object object, Class<?> clazz) {
    try {
      for (Field field : clazz.getDeclaredFields()) {
        Embedded embedded = field.getAnnotation(Embedded.class);
        if (embedded != null) {
          field.setAccessible(true);
          Object value = field.get(object);
          check(value, clazz);
        }
        validateMethods(new ReflectField(field, object));
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private void validatingErrorContexts(ReflectField reflectField, Validator validator) {
    for (ValidationMethodChecker validationMethodChecker : validator.getValidationList()) {
      validateField(reflectField, validationMethodChecker, validator.getErrorArea());
    }
  }

  private void validateField(ReflectField reflectField, ValidationMethodChecker validationMethodChecker, ErrorArea errorArea) {

    Annotation annotation = reflectField.getField().getAnnotation(validationMethodChecker.annotationValidation());
    if (annotation != null) {
      if (validationMethodChecker.annotationValidation().hashCode() != annotation.annotationType().hashCode()) {
        return;
      }
      if (!validationMethodChecker.annotationValidation().equals(annotation.annotationType())) {
        return;
      }
      validates(new ValidationContextImpl<>(reflectField, annotation, localeFieldName, language), validationMethodChecker, errorArea);
    }
  }

  private void validateMethods(ReflectField reflectField) {
    for (Validator validator : validatorCenter.getValidators()) {
      validatingErrorContexts(reflectField, validator);
    }
  }

  private void validates(ValidationContext<?> validationContext, ValidationMethodChecker validationMethodChecker, ErrorArea errorArea) {
    boolean checkedResult = validationMethodChecker.checkValidation(validationContext);
    if (!checkedResult) {
      FailTemplate failTemplate = errorArea.getFailTemplate(validationMethodChecker.failCode(), language);

      FailedFieldImpl failedField = new FailedFieldImpl();

      failedField.setMessage(failTemplate.render(validationContext.failArgs().toArray()));

      failedField.setField(validationContext.getName());

      checker.add(failedField);
    }
  }

  public Set<FailedField> getFailedFields() {
    return checker.getFailedFields();
  }
}
