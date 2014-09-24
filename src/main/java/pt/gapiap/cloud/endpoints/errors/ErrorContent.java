package pt.gapiap.cloud.endpoints.errors;

import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;

import java.util.Map;

public interface ErrorContent extends Iterable<Map.Entry<Integer, ErrorTemplate>>{
  String getLanguage();
  Map<String,?> getArgs();
}
