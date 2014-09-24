package pt.gapiap.servlets.language.en;

import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

import java.util.Iterator;
import java.util.Map;

public class UploadErrorsContentEN implements UploadErrorsContent {
  private Map<Integer, ErrorTemplate> map;

  public UploadErrorsContentEN() {
    init();
  }

  private void init() {
    map = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NO_ACTION_PARAMETER, new SimpleErrorTemplate("There is no parameter for the action"))
        .put(NO_UPLOAD_ACTION_REGISTERED, new ParametrizedErrorTemplate("There is no upload registered for the action '{0}'"))
        .put(KEY_NOT_NULL_STRING, new ParametrizedErrorTemplate("Key of method {0} must be a not null String"))
        .put(KEY_ALREADY_EXISTS, new ParametrizedErrorTemplate("The key {0} already exists"))
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
