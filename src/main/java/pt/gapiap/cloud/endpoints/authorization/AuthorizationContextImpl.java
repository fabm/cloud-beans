package pt.gapiap.cloud.endpoints.authorization;

public class AuthorizationContextImpl<T, A extends Authorization<?, ?>> implements AuthorizationContext<T, A> {
  private A authorization;
  private T service;

  public AuthorizationContextImpl(A authorization, T service) {
    this.authorization = authorization;
    this.service = service;
  }

  @Override
  public A getAuthorization() {
    return authorization;
  }

  @Override
  public T getService() {
    return service;
  }

}
