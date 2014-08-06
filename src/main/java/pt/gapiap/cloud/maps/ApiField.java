package pt.gapiap.cloud.maps;

import com.google.inject.Injector;
import pt.gapiap.proccess.logger.Logger;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import java.util.HashMap;
import java.util.Set;

class ApiField extends HashMap<String, AnnotationValueMap> implements ApiObject {
    private String name;
    @Inject
    private Logger logger;
    @Inject
    Injector injector;

    void loadField(Set<FieldAnnotation> fieldAnnotationSet) {
        logger.log("            :"+name+":"+fieldAnnotationSet.size()+"\n");
        for (FieldAnnotation fieldAnnotation : fieldAnnotationSet) {
            String name = fieldAnnotation.getApiValidation().getAlias();
            if(name.isEmpty()){
                name = fieldAnnotation.annotationMirror.getAnnotationType().toString();
            }
            AnnotationValueMap annotationValueMap = injector.getInstance(AnnotationValueMap.class);
            annotationValueMap.init(fieldAnnotation);
            put(name, annotationValueMap);
            logger.log("                :"+annotationValueMap+"\n");
        }
        logger.log("            :"+this+":"+"\n");
    }

    @Override
    public Type getType() {
        return Type.FIELD;
    }


    void setName(String name) {
        this.name = name;
    }
}
