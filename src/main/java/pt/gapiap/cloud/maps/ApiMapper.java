package pt.gapiap.cloud.maps;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.validation.ValidationMethod;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiMapper extends Hashtable<String, ApiMethod> {
  private Set<ApiValidationMap> validatorSet;
  @Inject
  private RoundEnvironment roundEnvironment;
  @Inject
  private Logger logger;
  @Inject
  private Injector injector;


  private ApiValidationMap getOrCreateValidator(List<? extends TypeMirror> typeMirrors) {
    for (ApiValidationMap apiValidationMap : validatorSet) {
      if (apiValidationMap.hasSameTypes(typeMirrors)) {
        return apiValidationMap;
      }
    }
    ApiValidationMap apiValidationMap = injector.getInstance(ApiValidationMap.class);
    apiValidationMap.setValidators(typeMirrors);
    apiValidationMap.init();
    validatorSet.add(apiValidationMap);
    return apiValidationMap;
  }

  private void addMethodsPath(String api, String methodName, ApiValidationMap apiValidationMap, TypeElement element) {
    ApiMethod method = get(api);

    WordsIterable wordsIterable = new WordsIterable(methodName);

    boolean newPath = method == null;
    ApiMethod next = null;

    if (newPath) {
      method = injector.getInstance(ApiMethod.class);
      put(api, method);
    }

    for (String word : wordsIterable) {
      boolean isLast = !wordsIterable.hasNext();
      if (isLast) {
        method.setApiValidationMap(apiValidationMap);
        method.setTypeElement(element);
        method.resolveMethod(word);
        return;
      }
      if (!newPath) {
        next = (ApiMethod) method.get(word);
        newPath = next == null;
      }
      if (!newPath) {
        method = next;
      } else {
        next = injector.getInstance(ApiMethod.class);
        method.put(word, next);
        method = next;
      }
    }
  }

  public void init() {
    validatorSet = new HashSet<>();
    Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(ApiMethodParameters.class);
    for (Element element : elementSet) {
      ApiMethodParameters annotation = element.getAnnotation(ApiMethodParameters.class);
      try {
        annotation.validators();
      } catch (MirroredTypesException e) {
        logger.log(String.format("%-60s:%-60s\n", annotation.api(), annotation.method()));
        ApiValidationMap apiValidationMap = getOrCreateValidator(e.getTypeMirrors());
        //this cast is possible because the target of @ApiMethodParameters Target is ElementType.TYPE
        addMethodsPath(annotation.api(), annotation.method(), apiValidationMap, (TypeElement) element);
      } catch (NullPointerException e) {
        //if the the annotation is null the error must be after the annotations processor to see the line where
        // the symbol it's unknown
      }
    }

    logger.log("ApiValidators \n\n");
    logger.log("\n");

    for (ApiValidationMap apiValidationMap : validatorSet) {
      for (TypeMirror typeMirror : apiValidationMap.getValidators()) {
        DeclaredType dt = (DeclaredType) typeMirror;
        TypeElement te = (TypeElement) dt.asElement();
        List<ExecutableElement> methods = ElementFilter.methodsIn(te.getEnclosedElements());
        for (ExecutableElement ee : methods) {
          logger.log(String.format("name  :%s\n", ee.getSimpleName()));
          Optional<ValidationMethod> optValidationMethodAnnotation = Optional.fromNullable(ee.getAnnotation(ValidationMethod.class));
          if(optValidationMethodAnnotation.isPresent()){
            logger.log("is ValidationMethod\n");
            logger.log(String.format("fail code:%d \n",optValidationMethodAnnotation.get().failCode()));
          }
        }
      }
    }
  }

  public String getJsonPreatifyApisMap() {
    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    return gson.toJson(this);
  }

  public String getJsonCompactApisMap() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public void printMethods() {
    Iterator<Map.Entry<String, ApiMethod>> es = entrySet().iterator();
    for (Map.Entry<String, ApiMethod> entry : entrySet()) {
      System.out.println(entry.getKey());
      print("    ", entry.getValue());
    }
  }

  private void print(String spc, ApiMethod apiMethod) {
    for (Map.Entry entry : apiMethod.entrySet()) {
      System.out.println(spc + entry.getKey());
      if (entry.getValue() != null && entry.getValue() instanceof ApiMethod) {
        print(spc + "    ", (ApiMethod) entry.getValue());
      }
    }
  }
}
