package pt.json.proccess.test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AnnotationTest {
    int testPrimitiveInt();
    Class<?> classTest();
}
