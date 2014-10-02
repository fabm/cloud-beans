package pt.gapiap.cloud.endpoints.authorization;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.common.base.Optional;
import com.google.common.reflect.Reflection;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.RequestScoped;
import pt.gapiap.cloud.endpoints.FailureManager;
import pt.gapiap.cloud.endpoints.errors.ErrorManager;
import pt.gapiap.runtime.reflection.EnumArrayFromAnnotation;
import pt.gapiap.servlets.OAuth2Helper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestScoped
public class ACLInvoker<T, A extends Authorization> {
  private Class<T> sint;
  private ServiceInvokeHandler serviceInvokeHandler;
  @Inject
  private HttpServletRequest servletRequest;
  @Inject
  private OAuth2Helper oAuth2Helper;
  @Inject
  private Injector injector;
  @Inject
  private ErrorManager errorManager;
  private A authorization;
  private String language;
  private FailureManager failureManager;

  public ACLInvoker(Class<T> sint) {
    this.sint = sint;
  }

  public void init(AuthorizationContext<T, A> authorizationContext, EnumArrayFromAnnotation enumArrayFromAnnotation) throws IOException, UnauthorizedException {
    String authString = servletRequest.getHeader("Authorization");
    Optional<String> op = Optional.fromNullable(Strings.emptyToNull(authString));

    String strBearer = "Bearer ";

    User user = null;

    if (op.isPresent() && authString.startsWith(strBearer)) {
      String token = authString.substring(strBearer.length());
      System.out.printf("email:%s\n", oAuth2Helper.getCurrentUserEmail(token));
      user = new User(oAuth2Helper.getCurrentUserEmail(token), "baby-help");
    }
    init(authorizationContext, enumArrayFromAnnotation, user);
  }

  public void init(AuthorizationContext<T, A> authorizationContext, EnumArrayFromAnnotation enumArrayFromAnnotation, User user) {
    this.authorization = authorizationContext.getAuthorization();
    authorizationContext.getAuthorization().init(user);
    serviceInvokeHandler = new ServiceInvokeHandler(authorizationContext, enumArrayFromAnnotation);
    this.language = servletRequest.getLocale().getLanguage();
    serviceInvokeHandler.setLanguage(this.language);
    this.failureManager = new FailureManager(language,errorManager);
    injector.injectMembers(serviceInvokeHandler);
  }

  public A getAuthorization() {
    return authorization;
  }

  public FailureManager getFailureManager(){
    return failureManager;
  }

  public T execute() {
    return Reflection.newProxy(sint, serviceInvokeHandler);
  }

}
