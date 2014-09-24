package pt.gapiap.cloud.endpoints.authorization;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;
import pt.gapiap.runtime.reflection.EnumArrayFromAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ServiceInvokeHandler implements InvocationHandler {
  private final EnumArrayFromAnnotation enumArrayFromAnnotation;
  @Inject
  private ErrorManager errorManager;
  private AuthorizationContext authorizationContext;
  private String language;

  ServiceInvokeHandler(AuthorizationContext authorizationContext, EnumArrayFromAnnotation enumArrayFromAnnotation) {
    this.authorizationContext = authorizationContext;
    this.enumArrayFromAnnotation = enumArrayFromAnnotation;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return callMethod(method, args);
    } catch (CEError cee) {
      return cee.getMap();
    } catch (RuntimeException rte) {
      rte.printStackTrace();
      return errorManager.create(GlobalContent.UNEXPECTED, this.language).getMap();
    }
  }

  private Object callMethod(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    Optional<Annotation> opAuthorizationAnnotation = Optional.fromNullable(method.getAnnotation(enumArrayFromAnnotation.getAnnotationClass()));

    if (opAuthorizationAnnotation.isPresent()) {
      Enum[] roles = enumArrayFromAnnotation.getEnumArray(opAuthorizationAnnotation.get());
      Authorization authorization = authorizationContext.getAuthorization();

      if (!authorization.hasRoles(roles)) {
        throw errorManager.create(GlobalContent.NOT_AUTHORIZED, language, method.getName());
      }
    }

    return method.invoke(authorizationContext.getService(), args);
  }


  public String getLanguage() {
    return language;
  }


  public void setLanguage(String language) {
    this.language = language;
  }
}
