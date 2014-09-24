package pt.gapiap.cloud.endpoints.errors.language.en;

import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;

import java.util.Iterator;
import java.util.Map;


public class GlobalContentEN implements GlobalContent {

  private Map<Integer, ErrorTemplate> map;

  public GlobalContentEN() {
    init();
  }

  private void init() {
    map = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NOT_AUTHORIZED, new ParametrizedErrorTemplate("You don't have previleges to enter in {0} area"))
        .put(NOT_AUTHENTICATED, new SimpleErrorTemplate("Please, authenticate with your account to access this area"))
        .put(UNEXPECTED, new SimpleErrorTemplate("Unexpected error occurred"))
        .build();
  }

  @Override
  public String getLanguage() {
    return "en";
  }

  @Override
  public Map<String, ?> getArgs() {
    return null;
  }

  @Override
  public Iterator<Map.Entry<Integer, ErrorTemplate>> iterator() {
    return map.entrySet().iterator();
  }
}
