package pt.gapiap.proccess.validation.defaultValidator.languages.pt;


import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.proccess.validation.defaultValidator.languages.DefaultValidatorErrorContent;

import javax.inject.Named;
import java.util.Iterator;
import java.util.Map;

public class DefaultValidatorErrorsContentPT implements DefaultValidatorErrorContent {
  private Map<Integer, ErrorTemplate> errorTemplates;


  public DefaultValidatorErrorsContentPT() {
    init();
  }

  private void init() {
    errorTemplates = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NOT_NULL, new ParametrizedErrorTemplate("O campo {0} é obrigatório"))
        .put(EMAIL, new ParametrizedErrorTemplate("O campo {0} não está de acordo com o formato de um email"))
        .put(SIZE, new ParametrizedErrorTemplate("O campo {0} deve ser inferior a {1} e superior a {2}"))
        .put(MAX, new ParametrizedErrorTemplate("O campo {0} deve ser inferior a {1}"))
        .put(MIN, new ParametrizedErrorTemplate("O campo {0} deve ser superior a {1}"))
        .build();
  }

  @Override
  public String getLanguage() {
    return "pt";
  }

  @Override
  public Map<String, ?> getArgs() {
    return null;
  }

  @Override
  public Iterator<Map.Entry<Integer, ErrorTemplate>> iterator() {
    return errorTemplates.entrySet().iterator();
  }
}
