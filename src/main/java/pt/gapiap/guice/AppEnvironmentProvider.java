package pt.gapiap.guice;


import com.google.appengine.api.utils.SystemProperty;
import pt.gapiap.cloud.endpoints.authorization.AppEnvironment;

import javax.inject.Provider;

public class AppEnvironmentProvider implements Provider<AppEnvironment> , AppEnvironment {

  @Override
  public AppEnvironment get() {
    return this;
  }

  @Override
  public boolean isProductionMode() {
    return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
  }

  @Override
  public boolean isDevMode() {
    return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
  }
}
