package pt.gapiap.cloud.maps;

import com.google.inject.Inject;
import pt.gapiap.proccess.logger.Logger;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.HashMap;
import java.util.Map;

class AnnotationValueMap extends HashMap<String, Object> implements ApiObject {
    private String name;
    @Inject
    private Logger logger;


    public void init(AnnotationMirror annotationMirror, String name) {
        this.name = name;
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            Object value = entry.getValue().getValue();
            String key = entry.getKey().getSimpleName().toString();
            logger.log("                " + key + ":" + value + ":" + value.getClass() + "\n");
            put(key, value);
        }
    }


    @Override
    public Type getType() {
        return Type.ANNOTATION_MIRROR_VALUE_MAP;
    }

    @Override
    public String getName() {
        return name;
    }
}
