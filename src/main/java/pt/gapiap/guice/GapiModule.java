package pt.gapiap.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import pt.gapiap.cloud.endpoints.authorization.AppEnvironment;
import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;
import pt.gapiap.cloud.endpoints.errors.ErrorArea;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.cloud.endpoints.errors.GlobalErrorArea;
import pt.gapiap.runtime.reflection.EnumArrayFromAnnotation;

import java.util.List;

public abstract class GapiModule<R extends Enum<R>, U extends UserWithRoles<R>> extends AbstractModule {

  protected abstract EnumArrayFromAnnotation<R, ?> getEnumArrayFromAnnotationClass();


  private Class<GlobalErrorArea> getGlobalErrorAreaClassOrSubstitute() {
    return GlobalErrorArea.class;
  }

  protected abstract Class<? extends AclInvokerTypeParameters> getInvokerTypeParameters();

  protected abstract List<? extends ErrorArea> getErrorAreas();

  @Override
  protected void configure() {
    bind(ErrorManager.class).toInstance(new ErrorManager(getErrorAreas()));
    bind(AppEnvironment.class).toProvider(AppEnvironmentProvider.class).in(RequestScoped.class);
    bind(getGlobalErrorAreaClassOrSubstitute()).in(Scopes.SINGLETON);
    bind(EnumArrayFromAnnotation.class).toInstance(getEnumArrayFromAnnotationClass());
    bind(AclInvokerTypeParameters.class).to(getInvokerTypeParameters());

    bind(LanguageProvider.class).in(RequestScoped.class);
    bind(String.class)
        .annotatedWith(Names.named("language"))
        .toProvider(LanguageProvider.class);
  }
}
