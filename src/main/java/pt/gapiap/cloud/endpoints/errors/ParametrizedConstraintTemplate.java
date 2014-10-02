package pt.gapiap.cloud.endpoints.errors;

import com.google.common.collect.ImmutableMap;

public class ParametrizedConstraintTemplate extends ParametrizedErrorTemplate {
  public ParametrizedConstraintTemplate(String message, String constraintName) {
    super(message, ImmutableMap.of("constraint",constraintName));
  }
}
