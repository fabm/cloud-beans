package pt.gapiap.proccess.validation.bean.checker;

import pt.gapiap.proccess.validation.ValidationMethod;
import pt.gapiap.runtime.reflection.ReflectionMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class ValidationMethodCheckerImpl implements ValidationMethodChecker {
  private Method method;
  private int priority;
  private Class<? extends Annotation> validationAnnotation;
  private Object validatorInstance;
  private int failCode;

  ValidationMethodCheckerImpl(Method method, ValidationMethod validationMethod, Object validatorInstance, int failCode) {
    this.method = method;
    this.failCode = failCode;
    this.validationAnnotation = validationMethod.value();
    this.priority = validationMethod.priority();
    this.validatorInstance = validatorInstance;
  }


  @Override
  public int compareTo(ValidationMethodChecker that) {
    if (this.getPriority() > that.getPriority()) {
      return -1;
    }
    if (this.getPriority() < that.getPriority()) {
      return 1;
    }
    return 0;
  }

  @Override
  public Class<? extends Annotation> annotationValidation() {
    return this.validationAnnotation;
  }

  @Override
  public int getPriority() {
    return this.priority;
  }

  @Override
  public int failCode() {
    return failCode;
  }

  @Override
  public boolean checkValidation(ValidationContext validationContext) {
    ReflectionMethod method = new ReflectionMethod(validatorInstance, this.method);
    return method.forceInvoke(validationContext);
  }
}
