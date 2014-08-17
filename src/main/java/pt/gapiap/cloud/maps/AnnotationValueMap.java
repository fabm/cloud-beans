package pt.gapiap.cloud.maps;

import com.google.inject.Inject;
import pt.gapiap.proccess.logger.Logger;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.HashMap;
import java.util.Map;

class AnnotationValueMap extends HashMap<String, Object> implements ApiObject {
    @Inject
    private Logger logger;


    public void init(FieldAnnotation fieldAnnotation) {
        int priority = fieldAnnotation.getApiValidation().getPriority();
        if (priority != 0) {
            put("priority", priority);
        }
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fieldAnnotation.getAnnotationMirror().getElementValues().entrySet()) {
            String key = entry.getKey().getSimpleName().toString();
            Object value = entry.getValue().getValue();
            logger.log("                " + key + ":" + value + ":" + value.getClass() + "\n");
            put(key, value);
        }
    }


    @Override
    public Type getType() {
        return Type.ANNOTATION_MIRROR_VALUE_MAP;
    }

}
