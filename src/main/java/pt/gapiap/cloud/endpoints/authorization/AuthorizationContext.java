package pt.gapiap.cloud.endpoints.authorization;


public interface AuthorizationContext<T, A extends Authorization> {

  A getAuthorization();

  T getService();
}
