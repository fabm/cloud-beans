package pt.gapiap.proccess.validation.defaultValidator.languages.en;


import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContent;

import java.util.Iterator;
import java.util.Map;

public class DefaultValidatorErrorsContentEN implements DefaultValidatorErrorContent {
  private Map<Integer, ErrorTemplate> map;

  public DefaultValidatorErrorsContentEN() {
    init();
  }

  private void init() {
    map = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NOT_NULL, new ParametrizedErrorTemplate("Field {0} is required"))
        .put(EMAIL, new ParametrizedErrorTemplate("Field {0} don''t have an email format"))
        .put(SIZE, new ParametrizedErrorTemplate("Field {0} must be lesser then {1} and greater then {2}"))
        .put(MAX, new ParametrizedErrorTemplate("Field {0} must be lesser then {1}"))
        .put(MIN, new ParametrizedErrorTemplate("Field {0} must be greater a {1}"))
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
