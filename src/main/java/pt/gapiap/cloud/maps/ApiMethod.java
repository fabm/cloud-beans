package pt.gapiap.cloud.maps;

import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.proccess.annotations.Embedded;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.utils.TypeUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ApiMethod extends HashMap<String, Object> implements ApiObject {
  @Inject
  private Logger logger;
  @Inject
  private Injector injector;
  private ApiValidationMap apiValidationMap;
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
      if (isEmbedded(variableElement)) {
        DeclaredType declaredType = (DeclaredType) variableElement.asType();
        loadElement((TypeElement) declaredType.asElement());
      } else {
        evaluateVariableElement(variableElement, apiValidationMap);
      }
    }
  }

  private boolean isEmbedded(VariableElement variableElement) {
    return variableElement.getAnnotation(Embedded.class) != null;
  }

  private void evaluateVariableElement(VariableElement variableElement, ApiValidationMap apiValidationMap) {
    Set<FieldAnnotation> fieldAnnotationSet = new HashSet<>();
    for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
      logger.log(String.format("%-60s:\n",annotationMirror.getAnnotationType()));
      FieldAnnotation fieldAnnotation = apiValidationMap.getFieldAnnotation(annotationMirror);
      if (fieldAnnotation != null) {
        fieldAnnotationSet.add(fieldAnnotation);
      }
    }
    String apiFieldName = variableElement.getSimpleName().toString();
    ApiField apiField = injector.getInstance(ApiField.class);
    apiField.setName(apiFieldName);
    logger.log(String.format("%-60s:\n", apiFieldName));
    put(apiFieldName, apiField.loadField(fieldAnnotationSet));
  }

  public void setApiValidationMap(ApiValidationMap apiValidationMap) {
    this.apiValidationMap = apiValidationMap;
  }


  public void resolveMethod(String name) {
    ApiMethod apiMethod = injector.getInstance(ApiMethod.class);
    apiMethod.setApiValidationMap(this.apiValidationMap);
    apiMethod.setTypeElement(typeElement);
    apiMethod.loadElement(apiMethod.typeElement);
    put(name, apiMethod);
  }

  public Element getTypeElement() {
    return typeElement;
  }

  void setTypeElement(TypeElement element) {
    this.typeElement = element;
  }

}
