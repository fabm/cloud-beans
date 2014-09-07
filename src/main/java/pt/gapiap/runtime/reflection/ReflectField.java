package pt.gapiap.runtime.reflection;

import java.lang.reflect.Field;

/**
 * {@inheritDoc}
 *
 * <p>
 * This implementation wraps the reflection actions between an {@linkplain java.lang.Object} and a {@linkplain java.lang.reflect.Field}
 * </p>
 */
public class ReflectField extends ReflectionElement {
  private Field field;

  /**
   * <p>
   * Create an instance of baseClass, and attribute the baseClass as it, like {@linkplain pt.gapiap.runtime.reflection.ReflectionElement#setBaseClass(Class)}
   * </p>
   *
   * @param baseClass class to instantiate
   * @throws pt.gapiap.runtime.reflection.ReflectionError
   */
  public ReflectField(Class<?> baseClass) {
    this.baseClass = baseClass;
    createInstance();
  }

  /**
   * @param field reflection object on which do the actions like {@linkplain pt.gapiap.runtime.reflection.ReflectField#forceSet(Object)} or
   * {@linkplain ReflectField#forceGet()}
   * @param object target object of reflection
   */
  public ReflectField(Field field, Object object) {
    this.field = field;
    this.object = object;
    baseClass = object.getClass();
  }

  /**
   * @param object target object of reflection
   */
  public ReflectField(Object object) {
    this.object = object;
    baseClass = object.getClass();
  }

  private void createInstance() {
    try {
      object = baseClass.newInstance();
    } catch (InstantiationException e) {
      throw new ReflectionError(e);
    } catch (IllegalAccessException e) {
      throw new ReflectionError(e);
    }
  }

  /**
   * <p>
   *  Attributes to field the value of the declaredField name in the base class calling the method
   *  {@linkplain java.lang.Class#getDeclaredField(String)} of baseClass in {@linkplain ReflectionElement#getBaseClass()}
   * </p>
   *
   * @param name of field in baseClass
   */
  public void setDeclaredFieldName(String name) {
    try {
      getBaseClass().getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new ReflectionError(e);
    }
  }

  /**
   *
   * @return
   */
  public Object forceGet() {
    field.setAccessible(true);
    try {
      return field.get(object);
    } catch (IllegalAccessException e) {
      throw new ReflectionError(e);
    }
  }

  public void forceSet(Object value) {
    field.setAccessible(true);
    try {
      field.set(object, value);
    } catch (IllegalAccessException e) {
      throw new ReflectionError(e);
    }
  }

  public Field getField() {
    return field;
  }

}
