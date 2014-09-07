package pt.gapiap.proccess.validation.bean.checker;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface ValidationContext<T extends Annotation> {

  Collection<Object> failArgs();

  boolean isNull();

  T getAnnotation();

  Class<?> getType();

  String getName();

  boolean isString();

  boolean isCollection();

  boolean isArryay();

  boolean isAPermittedNull(boolean permitted);

  boolean isEmptyString();

  Object getValue();

  void setValue(Object value);

  String getLocalFieldName();

  Object getContainer();
}
