package pt.gapiap.proccess.validation.bean.checker;


import pt.gapiap.proccess.validation.LocaleFieldName;
import pt.gapiap.runtime.reflection.ReflectField;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

public class ValidationContextImpl<T extends Annotation> implements ValidationContext<T> {
  ReflectField reflectField;
  private T annotation;
  private Collection<Object> failArgs;
  private LocaleFieldName localeFieldName;
  private String language;

  public ValidationContextImpl(
      ReflectField reflectField,
      T annotation,
      LocaleFieldName localeFieldName,
      String language
  ) {
    this.reflectField = reflectField;
    this.annotation = annotation;
    this.localeFieldName = localeFieldName;
    this.language = language;
    this.failArgs = new ArrayList<>();
  }

  @Override
  public Collection<Object> failArgs() {
    return failArgs;
  }

  @Override
  public boolean isNull() {
    return getValue() == null;
  }

  @Override
  public T getAnnotation() {
    return annotation;
  }

  @Override
  public Class<?> getType() {
    return reflectField.getField().getType();
  }

  @Override
  public String getName() {
    return reflectField.getField().getName();
  }

  @Override
  public boolean isString() {
    return getType() == String.class;
  }

  @Override
  public boolean isCollection() {
    return getType().isAssignableFrom(Collection.class);
  }

  @Override
  public boolean isArryay() {
    return getType().isArray();
  }

  @Override
  public boolean isAPermittedNull(boolean permitted) {
    return permitted && isNull();
  }

  @Override
  public boolean isEmptyString() {
    return getType() == String.class && getValue().toString().isEmpty();
  }

  @Override
  public Object getValue() {
    return reflectField.forceGet();
  }

  @Override
  public void setValue(Object value) {
    reflectField.forceSet(value);
  }

  @Override
  public String getLocalFieldName() {
    return localeFieldName.getLocaleFildName(getName(), reflectField.getBaseClass(), language);
  }

  @Override
  public Object getContainer() {
    return reflectField.getObject();
  }


}
