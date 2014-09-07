package pt.gapiap.runtime.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionMethod extends ReflectionElement{
  private Method method;

  public ReflectionMethod(Object object) {
    this.object = object;
    this.baseClass = object.getClass();
  }

  public ReflectionMethod(Object object,Method method) {
    this.method = method;
    this.object = object;
    this.baseClass = object.getClass();
  }


  public void setDeclaredMethod(String name){
    try {
      this.method = baseClass.getDeclaredMethod(name);
    } catch (NoSuchMethodException e) {
      throw new ReflectionError(e);
    }
  }

  public <R>R forceInvoke(Object... args) {
    try {
      method.setAccessible(true);
      return (R) method.invoke(object, args);
    } catch (IllegalAccessException e) {
      throw new ReflectionError(e);
    } catch (InvocationTargetException e) {
      throw new ReflectionError(e);
    }
  }
}
