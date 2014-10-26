package pt.gapiap.cloud.endpoints.authorization;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import pt.gapiap.cloud.endpoints.EndpointReturn;
import pt.gapiap.cloud.endpoints.errors.CEError;
import pt.gapiap.cloud.endpoints.errors.FailureManager;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;
import pt.gapiap.runtime.reflection.EnumArrayFromAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServiceInvokeHandler<R extends Enum<R>, U extends UserWithRoles<R>, A extends Annotation, S> implements InvocationHandler {
  private static Logger LOGGER = Logger.getLogger(ServiceInvokeHandler.class.getName());
  @Inject
  private FailureManager failureManager;
  @Inject
  private EnumArrayFromAnnotation<R, A> enumArrayFromAnnotation;
  @Inject
  private Authorization<R> authorization;
  @Inject
  private S service;


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return callMethod(method, args);
    } catch (CEError cee) {
      LOGGER.log(Level.FINE, "known exception");
      return new EndpointReturn(cee.getFailure());
    } catch (RuntimeException rte) {
      LOGGER.log(Level.SEVERE, "known exception");
      LOGGER.throwing(this.getClass().getName(), "invoke", rte);
      return new EndpointReturn(failureManager.createFailure(GlobalContent.UNEXPECTED));
    }
  }

  private Object callMethod(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
    Optional<A> opAuthorizationAnnotation = Optional.fromNullable(method.getAnnotation(enumArrayFromAnnotation.getAnnotationClass()));


    if (opAuthorizationAnnotation.isPresent()) {
      R[] roles = enumArrayFromAnnotation.getEnumArray(opAuthorizationAnnotation.get());

      if (!authorization.hasRoles(roles)) {
        throw failureManager.createError(GlobalContent.NOT_AUTHORIZED, method.getName());
      }
    }
    if (method != null) {
      LOGGER.log(Level.FINE, "method called:"+method.toString());
    }else{
      LOGGER.log(Level.FINE, "method is null");
    }
    return method.invoke(service, args);
  }

}
