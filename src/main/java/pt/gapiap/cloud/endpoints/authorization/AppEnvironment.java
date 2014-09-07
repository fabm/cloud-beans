package pt.gapiap.cloud.endpoints.authorization;

public interface AppEnvironment {
  boolean isProductionMode();
  boolean isDevMode();
}
