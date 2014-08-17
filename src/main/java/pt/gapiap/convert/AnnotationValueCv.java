package pt.gapiap.convert;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.type.MirroredTypeException;

public class AnnotationValueCv implements AnnotationValue {
    private Object value;

    public AnnotationValueCv(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        if(value instanceof Class<?>){
            throw new MirroredTypeException(new DeclaredTypeCv((Class<?>) value));
        }
        return value;
    }

    @Override
    public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
        return null;
    }

}
