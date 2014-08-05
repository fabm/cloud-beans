package pt.gapiap.proccess.wrappers;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ApiFieldsIterable implements Iterable<ApiField>, Iterator<ApiField> {

    private ApiMethodPMW apiMethodPMW;
    private Iterator<VariableElement> iteratorVA;
    private Set<Validation> validations;
    private ApiField apiField;
    private Stack<Iterator<VariableElement>> stack = new Stack<>();

    public ApiFieldsIterable(ApiMethodPMW apiMethodPMW) {
        this.apiMethodPMW = apiMethodPMW;
        init();
    }


    private void init() {
        Validator validator = Validator.getValidator(apiMethodPMW.getValidator());
        validations = validator.getValidations();
        List<VariableElement> vaList = ElementFilter.fieldsIn(apiMethodPMW.getElement().getEnclosedElements());
        iteratorVA = vaList.iterator();
        if (!apiMethodPMW.getSuperTypes().isEmpty()) {
            for (TypeMirror typeMirror : apiMethodPMW.getSuperTypes()) {
                stack.push(iteratorVA);
                iteratorVA = ElementFilter.fieldsIn(((DeclaredType) typeMirror).asElement().getEnclosedElements()).iterator();
            }
        }
    }

    @Override
    public Iterator<ApiField> iterator() {
        return this;
    }

    private boolean nextFieldApi(VariableElement ve) {
        ValidationRulesIteratable validationRulesIteratable = new ValidationRulesIteratable(ve, validations);
        if (validationRulesIteratable.iterator().hasNext()) {
            apiField = new ApiField();
            apiField.setName(ve.getSimpleName().toString());
            apiField.validationRuleIterable = validationRulesIteratable;
            return true;
        }
        return hasNext();
    }

    @Override
    public boolean hasNext() {
        if (iteratorVA.hasNext()) {
            VariableElement nextVa = iteratorVA.next();
            if (nextFieldApi(nextVa)) {
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
        return apiField;
    }

    @Override
    public void remove() {
        throw new NoSuchMethodError("There no such method support");
    }

}
