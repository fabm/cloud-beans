package pt.gapiap.proccess.validation;

public class LocaleFieldNameDefault implements LocaleFieldName {
  @Override
  public String getLocaleFildName(String fieldName, Class<?> baseClass, String language) {
    return fieldName;
  }
}
