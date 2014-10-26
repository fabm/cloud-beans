package pt.gapiap.cloud.endpoints.authorization;

public interface AuthorizationEvents {
  void devMode();

  void loadUserWithRoles(String email);
}
