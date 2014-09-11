package pt.gapiap.cloud.endpoints.authorization;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.GlobalError;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ServiceInvokeHandler implements InvocationHandler {
  private final RolesCapturer rolesCapturer;
  @Inject
  private GlobalError globalError;
  private AuthorizationContext authorizationContext;
  private String language;

  ServiceInvokeHandler(AuthorizationContext authorizationContext, RolesCapturer rolesCapturer) {
    this.authorizationContext = authorizationContext;
    this.rolesCapturer = rolesCapturer;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return callMethod(method, args);
    } catch (CEError e) {
      return e.getMap();
    }
  }

  private Object callMethod(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    try {
      Optional<Annotation> opAuthorizationAnnotation = Optional.fromNullable(method.getAnnotation(rolesCapturer.getAnnotationClass()));

      if (opAuthorizationAnnotation.isPresent()) {
        Enum[] roles = rolesCapturer.getRoles(opAuthorizationAnnotation.get());
        Authorization authorization = authorizationContext.getAuthorization();

        if (!authorization.hasRoles(roles)) {
          throw globalError.create(GlobalContent.NOT_AUTHORIZED, language, method.getName());
        }
      }

      return method.invoke(authorizationContext.getService(), args);
    } catch (RuntimeException e) {
      throw globalError.create(GlobalContent.UNEXPECTED, language);
    }
  }


  public String getLanguage() {
    return language;
  }


  public void setLanguage(String language) {
    this.language = language;
  }
}
