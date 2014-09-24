package pt.gapiap.cloud.endpoints.errors.language.pt;

import com.google.common.collect.ImmutableMap;
import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.ParametrizedErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.SimpleErrorTemplate;
import pt.gapiap.cloud.endpoints.errors.language.GlobalContent;

import java.util.Iterator;
import java.util.Map;


public class GlobalContentPT implements GlobalContent {
  private Map<Integer, ErrorTemplate> map;

  public GlobalContentPT() {
    init();
  }

  private void init() {
    map = ImmutableMap.<Integer, ErrorTemplate>builder()
        .put(NOT_AUTHORIZED, new ParametrizedErrorTemplate("O utilizador não está autorizado a aceder ao metodo {0}"))
        .put(NOT_AUTHENTICATED, new SimpleErrorTemplate("O utilizador não está autenticado"))
        .put(UNEXPECTED, new SimpleErrorTemplate("Ocorreu um erro inesperado"))
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
