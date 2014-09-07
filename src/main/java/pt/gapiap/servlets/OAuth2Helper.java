package pt.gapiap.servlets;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.repackaged.org.codehaus.jackson.JsonNode;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

import java.io.IOException;

public class OAuth2Helper {
  private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /**
   * Call the url, that uses a OAuth2 to get the current email user
   *
   * @param accessToken String representation of the Bearer token
   * @return current user email
   * @throws java.io.IOException
   * @throws org.apache.http.client.HttpResponseException
   */
  public String getCurrentUserEmail(String accessToken) throws IOException, UnauthorizedException {
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

}
