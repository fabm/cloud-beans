package pt.gapiap.proccess.validation.bean.checker;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.errors.ErrorArea;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.validation.ValidationClass;
import pt.gapiap.proccess.validation.ValidationMethod;
import pt.gapiap.runtime.reflection.ReflectField;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValidatorCenter {
  private String api;
  private String method;
  private Set<Validator> validators;
  @Inject
  private Injector injector;

  public void init(Object pojo) {
    checkNotNull(pojo, "pojo must be not null");
    ApiMethodParameters apiMethodParameters = pojo.getClass().getAnnotation(ApiMethodParameters.class);

    checkNotNull(apiMethodParameters, "apiMethodParameters must be not null");

    this.api = apiMethodParameters.api();
    this.method = apiMethodParameters.method();

    iterateValidationClasses(apiMethodParameters.validators());
  }

  public String getApi() {
    return api;
  }

  public String getMethod() {
    return method;
  }


  public Collection<Validator> getValidators() {
    return validators;
  }

  private void iterateValidationClasses(Class<?>[] validationClasses) {
    this.validators = new HashSet<>();

    for (Class<?> validatorClass : validationClasses) {
      validators.add(createValidationErrorContext(new ReflectField(validatorClass)));
    }
  }

  private Validator createValidationErrorContext(ReflectField reflectField) {
    Class<?> validationClass = reflectField.getBaseClass();
    ValidationClass validationClassAnnotation = validationClass.getAnnotation(ValidationClass.class);
    checkNotNull(validationClassAnnotation, "A validator must have the annotation " + ValidationClass.class.getName() + "annotation");
    Class<? extends ErrorArea> errorAreaClass = validationClassAnnotation.value();

    List<ValidationMethodChecker> list = new ArrayList<>();
    for (Method method : validationClass.getDeclaredMethods()) {
      Optional<ValidationMethod> optValidationMethod = Optional.fromNullable(method.getAnnotation(ValidationMethod.class));

      if (optValidationMethod.isPresent()) {
        ValidationMethod validationMethod = optValidationMethod.get();
        int failCode = validationMethod.failCode();
        ValidationMethodChecker validationMethodChecker = new ValidationMethodCheckerImpl(method, validationMethod, reflectField.getObject(), failCode);
        list.add(validationMethodChecker);
      }

    }

    return new ValidatorImpl(validationClass, injector.getInstance(errorAreaClass), list);
  }

}
