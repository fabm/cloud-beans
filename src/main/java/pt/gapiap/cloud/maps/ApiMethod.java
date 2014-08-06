package pt.gapiap.cloud.maps;

import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.utils.TypeUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ApiMethod extends HashMap<String, ApiObject> implements ApiObject {
    @Inject
    private Logger logger;
    @Inject
    private Injector injector;
    private ApiValidator apiValidator;
    private TypeElement typeElement;


    @Override
    public Type getType() {
        return Type.METHOD;
    }


    private void loadElement(TypeElement typeElement) {
        TypeElement superTypeElement = TypeUtils.getFromTEtoSuperTE(typeElement);
        if (superTypeElement != null) {
            loadElement(superTypeElement);
        } else {
            //the last super class one is Object.class
            return;
        }
        for (VariableElement variableElement : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
            evaluateVariableElement(variableElement, apiValidator);
        }
    }

    private void evaluateVariableElement(VariableElement variableElement, ApiValidator apiValidator) {
        Set<FieldAnnotation> fieldAnnotationSet = new HashSet<>();
        for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
            logger.log("            :" + annotationMirror.getAnnotationType() + "\n");
            FieldAnnotation fieldAnnotation = apiValidator.getFieldAnnotation(annotationMirror);
            if (fieldAnnotation!=null) {
                fieldAnnotationSet.add(fieldAnnotation);
            }
        }
        String apiFieldName = variableElement.getSimpleName().toString();
        ApiField apiField = injector.getInstance(ApiField.class);
        apiField.setName(apiFieldName);
        apiField.loadField(fieldAnnotationSet);
        logger.log("            :" + apiFieldName + "\n");
        put(apiFieldName, apiField);
    }


    public void setApiValidator(ApiValidator apiValidator) {
        this.apiValidator = apiValidator;
    }


    public void resolveMethod(String name) {
        ApiMethod apiMethod = injector.getInstance(ApiMethod.class);
        apiMethod.setApiValidator(this.apiValidator);
        apiMethod.setTypeElement(typeElement);
        apiMethod.loadElement(apiMethod.typeElement);
        logger.log("            :" + apiMethod.size() + "\n");
        logger.log("            :" + apiMethod + "\n");
        put(name, apiMethod);
    }

    public Element getTypeElement() {
        return typeElement;
    }

    void setTypeElement(TypeElement element) {
        this.typeElement = element;
    }

}
