package pt.gapiap.cloud.maps;


import javax.lang.model.type.DeclaredType;

public class ApiValidation {
  int failCode;
  int priority;
  DeclaredType annotationType;

  public int getPriority() {
    return priority;
  }

  public int getFailCode(){
    return failCode;
  }

  public DeclaredType getAnnotationType() {
    return annotationType;
  }
}
