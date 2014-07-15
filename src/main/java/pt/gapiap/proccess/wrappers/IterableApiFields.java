package pt.gapiap.proccess.wrappers;

import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.proccess.mirrors.annotationMirror.AnnotationMirrorLoader;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.*;

public class IterableApiFields implements Iterable<ApiField>, Iterator<ApiField> {

    private ApiMethodPMW apiMethodPMW;
    private Iterator<VariableElement> iteratorVA;
    private Set<Validation> validations;
    private ApiField apiField;
    private Stack<Iterator<VariableElement>> stack = new Stack<>();


    public IterableApiFields(ApiMethodPMW apiMethodPMW) {
        this.apiMethodPMW = apiMethodPMW;
        init();
    }

    private void init() {
        Validator validator = Validator.getValidator(apiMethodPMW.getValidator());
        validations = validator.getValidations();
        List<VariableElement> vaList = ElementFilter.fieldsIn(apiMethodPMW.getElement().getEnclosedElements());
        iteratorVA = vaList.iterator();
        if(!apiMethodPMW.getSuperTypes().isEmpty()){
            for(TypeMirror typeMirror:apiMethodPMW.getSuperTypes()){
                stack.push(iteratorVA);
                iteratorVA = ElementFilter.fieldsIn(((DeclaredType)typeMirror).asElement().getEnclosedElements()).iterator();
            }
        }
    }

    @Override
    public Iterator<ApiField> iterator() {
        return this;
    }

    private ApiField getApiField() {
        if (apiField == null) {
            apiField = new ApiField();
        }
        return apiField;
    }

    private boolean nextFieldApi(VariableElement ve) {
        Logger.get().log(ve.getSimpleName()+"\n");
        Map<String, Object> validationMap = new HashMap<>();
        for (Validation validation : validations) {
            for (AnnotationMirror annotationMirror : ve.getAnnotationMirrors()) {
                if (annotationMirror.getAnnotationType().toString().equals("pt.gapiap.proccess.annotations.Embedded")) {
                    stack.push(iteratorVA);
                    List<VariableElement> list = ElementFilter.fieldsIn(((DeclaredType)ve.asType()).asElement().getEnclosedElements());
                    iteratorVA = list.iterator();
                    Logger.get().log(":"+list.size()+"\n");
                    return false;
                } else if (annotationMirror.getAnnotationType().toString().equals(validation.getDeclaredType().toString())) {
                    validationMap.put(validation.getDeclaredType().toString(), AnnotationMirrorLoader.toMap(annotationMirror));
                    getApiField().setName(ve.getSimpleName().toString());
                    getApiField().setValidationRules(validationMap);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasNext() {
        while (iteratorVA.hasNext()) {
            if (nextFieldApi(iteratorVA.next())) {
                return true;
            }
        }
        if (stack.empty()) {
            return false;
        } else {
            iteratorVA = stack.pop();
            return hasNext();
        }
    }

    @Override
    public ApiField next() {
        ApiField apiFieldRet = apiField;
        apiField = null;
        Logger.get().log("name in next:" + apiFieldRet.getName() + "\n");
        return apiFieldRet;
    }

    @Override
    public void remove() {
        throw new NoSuchMethodError("There no such method support");
    }
}
