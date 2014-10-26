package pt.gapiap.guice;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.repackaged.org.codehaus.jackson.JsonNode;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OAuth2Provider implements Provider<String> {
  private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  @Inject
  private Injector injector;
  @Nullable
  private String currentEmail;

  private String getCurrentUserEmail(String accessToken) throws UnauthorizedException, IOException {
    if (accessToken == null) {
      return null;
    }
    GenericUrl userInfo = new GenericUrl("https://www.googleapis.com/userinfo/v2/me");
    Credential credential =
        new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);
    HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);

    HttpResponse httpResponse = null;
    try {
      httpResponse = requestFactory.buildGetRequest(userInfo).execute();
    } catch (HttpResponseException e) {
      if (e.getStatusCode() == 401) {
        throw new UnauthorizedException("Unauthorized");
      }
    }
    ObjectMapper om = new ObjectMapper();
    JsonNode jsonNode = om.readTree(httpResponse.getContent());
    return jsonNode.get("email").toString();
  }

  public void loadCurrentUserEmail() throws IOException, UnauthorizedException {
    HttpServletRequest servletRequest = injector.getInstance(HttpServletRequest.class);
    String authString = servletRequest.getHeader("Authorization");
    Optional<String> op = Optional.fromNullable(Strings.emptyToNull(authString));

    String strBearer = "Bearer ";

    if (op.isPresent() && authString.startsWith(strBearer)) {
      String token = authString.substring(strBearer.length());
      currentEmail = getCurrentUserEmail(token);
    }
  }

  @Override
  public String get() {
    return currentEmail;
  }
}
