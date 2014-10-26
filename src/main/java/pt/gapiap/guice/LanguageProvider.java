package pt.gapiap.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;

import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class LanguageProvider implements Provider<String>{

  private String language;

  @Inject
  public LanguageProvider(HttpServletRequest httpServletRequest) {
    this.language = httpServletRequest.getLocale().getLanguage();
  }

  @Override
  public String get() {
    return language;
  }
}
