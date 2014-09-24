package pt.gapiap.cloud.maps;

import com.google.api.client.util.Maps;
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
    this.put("priority", fieldAnnotation.getApiValidation().getPriority());
    Map<String, Object> annotationArgs = Maps.newHashMap();
    logger.log(String.format("%-60s\n", fieldAnnotation.getAnnotationMirror().getAnnotationType()));
    logger.log(String.format("%-60s\n", fieldAnnotation.getAnnotationMirror().getElementValues().size()));
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fieldAnnotation.getAnnotationMirror().getElementValues().entrySet()) {
      String key = entry.getKey().getSimpleName().toString();
      Object value = entry.getValue().getValue();
      logger.log("                " + key + ":" + value + ":" + value.getClass() + "\n");
      annotationArgs.put(key, value);
    }
    if (annotationArgs.size() > 0) {
      this.put("args", annotationArgs);
    }
  }


  @Override
  public Type getType() {
    return Type.ANNOTATION_MIRROR_VALUE_MAP;
  }

}
