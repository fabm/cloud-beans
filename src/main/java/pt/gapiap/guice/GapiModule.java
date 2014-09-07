package pt.gapiap.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import pt.gapiap.cloud.endpoints.errors.GlobalError;
import pt.gapiap.errors.ErrorManager;

public class GapiModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(ErrorManager.class).in(Scopes.SINGLETON);
    bind(GlobalError.class).in(Scopes.SINGLETON);
  }
}
