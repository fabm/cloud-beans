package pt.gapiap.proccess.wrappers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AnnotationWrapper {
    protected AnnotationMirror annotationMirror;
    private StringBuilder stringBuilder;
    public Element element;
    private List<? extends TypeMirror> superTypes;


    private Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> getAnnotationEntryIterator(){
        return annotationMirror.getElementValues().entrySet().iterator();
    }

    public Element getElement() {
        return element;
    }

    void init() {
        Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> iterator
                = getAnnotationEntryIterator();
        stringBuilder = new StringBuilder();
        while (iterator.hasNext()){
            Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry =
                    iterator.next();
            stringBuilder.append("[key:");
            stringBuilder.append(entry.getKey().getSimpleName());
            stringBuilder.append("][value:");
            stringBuilder.append(entry.getValue().getValue().toString());
            stringBuilder.append("]\n");
            annotationEntry(entry);
        }
    }

    protected boolean annotationEntry(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry) {
        return filterAnnotationValue(
                entry.getKey().getSimpleName().toString(),
                entry.getValue().getValue()
        );
    }

    protected abstract boolean filterAnnotationValue(String key, Object value);

    public String getName() {
        return annotationMirror.getAnnotationType().toString();
    }

    public String representAnnotation(){
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+representAnnotation();
    }

    public void setSuperTypes(List<? extends TypeMirror> superTypes) {
        this.superTypes = superTypes;
    }

    public List<? extends TypeMirror> getSuperTypes() {
        return superTypes;
    }
}
