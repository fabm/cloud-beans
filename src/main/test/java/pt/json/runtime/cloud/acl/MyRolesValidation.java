package pt.json.runtime.cloud.acl;

import pt.json.runtime.cloud.authorization.MyRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MyRolesValidation {
  MyRole[] value() default {};
}
