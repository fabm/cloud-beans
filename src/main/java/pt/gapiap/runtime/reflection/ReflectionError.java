package pt.gapiap.runtime.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * Instead of throw exceptions like {@linkplain java.lang.InstantiationException} or {@linkplain java.lang.IllegalAccessException},
 * if you directly use the compile classes, instead of load the classes in runtime, you will appreciate the non intrusive exception treatment
 * otherwise you may threat with non required treatment
 *
 * @see java.lang.InstantiationException
 * @see java.lang.IllegalAccessException
 * @see java.lang.reflect.InvocationTargetException
 * @see java.lang.NoSuchMethodException
 *
 */
public class ReflectionError extends RuntimeException{
  private Exception e;

  public ReflectionError(Exception e) {
    super("Reflection error",e);
    this.e = e;
  }

  /**
   * @return the exception cause of reflection like {@linkplain java.lang.InstantiationException}, {@linkplain java.lang.reflect.InvocationTargetException},
   * {@linkplain java.lang.NoSuchMethodException} or {@linkplain java.lang.IllegalAccessException}
   */
  public Exception getExceptionCause() {
    return e;
  }
}
