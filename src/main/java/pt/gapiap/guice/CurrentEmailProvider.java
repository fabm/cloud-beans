package pt.gapiap.guice;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import javax.annotation.Nullable;
import java.io.IOException;

public class CurrentEmailProvider implements Provider<String> {
  @Nullable
  private String userEmail;
  @Inject
  private OAuth2Provider oAuth2Provider;
  @Inject
  private Injector injector;

  public void setEmail(@Nullable String userEmail) {
    this.userEmail = userEmail;
  }

  public void loadFromOAuth2() throws IOException, UnauthorizedException {
    oAuth2Provider.loadCurrentUserEmail();
    userEmail = oAuth2Provider.get();
  }

  @Override
  @Nullable
  public String get() {
    return userEmail;
  }
}
