package pt.gapiap.cloud.maps;

import pt.gapiap.utils.TypeUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ApiMethodFieldMap extends HashMap<String, ApiField> implements ApiObject {
    private String name;

    void loadElement(TypeElement typeElement, ApiValidator apiValidator) {
        TypeElement superTypeElement = TypeUtils.getFromTEtoSuperTE(typeElement);
        if (superTypeElement != null) {
            loadElement(superTypeElement, apiValidator);
        } else {
            //the last super class one is Object.class
            return;
        }
        for (VariableElement variableElement : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
            evaluateVariableElement(variableElement, apiValidator);
        }
    }

    private void evaluateVariableElement(VariableElement variableElement, ApiValidator apiValidator) {
        Set<AnnotationMirror> annotationMirrorSet = new HashSet<>();
        for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
            if (apiValidator.hasAnnotation(annotationMirror)) {
                annotationMirrorSet.add(annotationMirror);
            }
        }
        String apiFieldName = variableElement.getSimpleName().toString();
        ApiField apiField = new ApiField();
        apiField.setName(apiFieldName);
        apiField.loadField(annotationMirrorSet);
        put(apiFieldName, apiField);
    }


    @Override
    public Type getType() {
        return Type.FIELD;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}