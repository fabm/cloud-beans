package pt.gapiap.runtime.reflection;

/**
 * Abstract class to wrap the actions in the object with a base class
 */
public abstract class ReflectionElement {
  protected Object object;
  protected Class<?> baseClass;

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public Class<?> getBaseClass() {
    return baseClass;
  }

  /**
   * <p>Ex: in a scenario which you have a class inheritance</p>
   * <p>like above:</p>
   *
   * <pre>
   *
   *  SuperBaseClass instance = new BaseClass();
   *
   *  ReflectField reflectField = new ReflectedField(BaseClass.class);
   * </pre>
   *
   * the reflection actions like {@linkplain ReflectField#forceGet()} or {@linkplain pt.gapiap.runtime.reflection.ReflectField#forceSet(Object)} will be
   * applied to BaseClass.class
   *
   * @param baseClass the class which its apply the reflection action
   */
  public void setBaseClass(Class<?> baseClass) {
    this.baseClass = baseClass;
  }


}
