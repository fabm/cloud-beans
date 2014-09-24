package pt.gapiap.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import pt.gapiap.cloud.endpoints.errors.GlobalErrorArea;

public class GapiModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(GlobalErrorArea.class).in(Scopes.SINGLETON);
  }
}
