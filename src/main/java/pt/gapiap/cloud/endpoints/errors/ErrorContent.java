package pt.gapiap.cloud.endpoints.errors;

import pt.gapiap.cloud.endpoints.errors.ErrorTemplate;

public interface ErrorContent {
  String getLanguage();
  ErrorTemplate[] getErrorTemplates();
}
