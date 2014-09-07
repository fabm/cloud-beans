package pt.gapiap.proccess.validation.bean.checker;

import java.lang.annotation.Annotation;

/**
 * class to wrap methods to facilitate the comparison and save data of annotation
 * {@link pt.gapiap.proccess.validation.ValidationMethod}
 */
public interface ValidationMethodChecker extends Comparable<ValidationMethodChecker> {
  Class<? extends Annotation> annotationValidation();

  int getPriority();

  int failCode();

  boolean checkValidation(ValidationContext validationContext);
}
