package pt.gapiap.proccess.wrappers;

import javax.lang.model.type.DeclaredType;

public class ValidationMethodWrapper extends AnnotationWrapper {
    private DeclaredType declaredType;

    public DeclaredType getDeclaredType() {
        return declaredType;
    }

    @Override
    protected boolean filterAnnotationValue(String key, Object value) {
        if(key.equals("value")){
            declaredType = (DeclaredType) value;
            return true;
        }
        return false;
    }

    public String getValidator(){
        return getElement().getEnclosingElement().getSimpleName().toString();
    }


}
