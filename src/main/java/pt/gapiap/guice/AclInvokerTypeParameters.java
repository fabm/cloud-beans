package pt.gapiap.guice;

import pt.gapiap.cloud.endpoints.authorization.UserWithRoles;

import java.lang.annotation.Annotation;

public interface AclInvokerTypeParameters {
  Class<? extends Enum<?>> getRoleClass();
  Class<? extends UserWithRoles> getUserWithRolesClass();
  Class<? extends Annotation> getAnnotation();
}
