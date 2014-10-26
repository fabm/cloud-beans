package pt.gapiap.cloud.endpoints.authorization;

import com.google.common.reflect.Reflection;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;

import java.lang.annotation.Annotation;

@RequestScoped
public class ACLInvoker<R extends Enum<R>, U extends UserWithRoles<R>, A extends Annotation, S> {
  private ServiceInvokeHandler<R, U, A, S> serviceInvokeHandler;
  @Inject
  private TypeLiteral<S> serviceType;

  @Inject
  private void inject(ServiceInvokeHandler<R,U,A,S> serviceInvokeHandler){
    this.serviceInvokeHandler = serviceInvokeHandler;
  }

  public S execute() {
    return Reflection.newProxy((Class<S>)serviceType.getRawType(), serviceInvokeHandler);
  }

}
