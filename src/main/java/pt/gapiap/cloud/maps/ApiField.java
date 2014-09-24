package pt.gapiap.cloud.maps;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.inject.Injector;
import pt.gapiap.proccess.logger.Logger;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

class ApiField implements ApiObject {
  @Inject
  Injector injector;
  private String name;
  @Inject
  private Logger logger;

  Map<Integer, ?> loadField(Set<FieldAnnotation> fieldAnnotationSet) {
    Map<Integer, Object> map = Maps.newHashMap();
    for (FieldAnnotation fieldAnnotation : fieldAnnotationSet) {
      AnnotationValueMap annotationValueMap = injector.getInstance(AnnotationValueMap.class);
      annotationValueMap.init(fieldAnnotation);
      map.put(fieldAnnotation.apiValidation.failCode, annotationValueMap);
    }
    return map;
  }

  @Override
  public Type getType() {
    return Type.FIELD;
  }


  void setName(String name) {
    this.name = name;
  }
}
