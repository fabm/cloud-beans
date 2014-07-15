package pt.gapiap.proccess.mirrors.annotationMirror;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMirrorLoader {
    public static Map<String,Object> toMap(AnnotationMirror annotationMirror){
        Map<String, Object> mappedValues = new HashMap<>();

        for (Map.Entry<?, ?> me : annotationMirror.getElementValues().entrySet()) {
            ExecutableElement executableElement = (ExecutableElement) me.getKey();
            AnnotationValue annotationValue = (AnnotationValue) me.getValue();
            mappedValues.put(executableElement.getSimpleName().toString(), annotationValue.getValue());
        }
        return mappedValues;
    }

    public static <T> T loader(T pojo, AnnotationMirror annotationMirror, String expected) throws UnexpectedAnnotation {
        String strType = annotationMirror.getAnnotationType().toString();
        if (!strType.equals(expected))
            throw new UnexpectedAnnotation(annotationMirror, expected);
        Map<String, Object> mappedValues = toMap(annotationMirror);

        Class<? extends Object> cl = pojo.getClass();
        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            AnnotationMirrorMapper mirrorMapper = field.getAnnotation(AnnotationMirrorMapper.class);
            if (mirrorMapper != null) {
                Object value = mappedValues.get(mirrorMapper.value());
                try {
                    if (value != null) {
                        field.set(pojo, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return pojo;
    }

}
