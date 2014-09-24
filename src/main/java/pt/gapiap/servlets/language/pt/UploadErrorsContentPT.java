package pt.gapiap.servlets.language.pt;


import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.servlets.language.UploadErrorsContent;

import java.util.Iterator;
import java.util.Map;

public class UploadErrorsContentPT implements UploadErrorsContent {

  private Map<Integer, ErrorTemplate> map;

  public UploadErrorsContentPT() {
    init();
  }

  private void init() {
    map = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NO_ACTION_PARAMETER, new SimpleErrorTemplate("Não existem parametros para a ação"))
        .put(NO_UPLOAD_ACTION_REGISTERED, new ParametrizedErrorTemplate("Não foi registado upload para a ação '{0}'"))
        .put(KEY_NOT_NULL_STRING, new ParametrizedErrorTemplate("A chave do método {0} tem que ser uma string não vazia"))
        .put(KEY_ALREADY_EXISTS, new ParametrizedErrorTemplate("A chave '{0}' já existe"))
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
    return map.entrySet().iterator();
  }
}
