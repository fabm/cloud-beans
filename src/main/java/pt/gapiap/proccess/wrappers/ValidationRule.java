package pt.gapiap.proccess.wrappers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Iterator;
import java.util.Map;

public class ValidationRule {
    private AnnotationMirror annotationMirror;
    private Iterable<AnnotationMirrorAtribute> annotationMirrorAtributes;

    public String getAnnotationMirrorName() {
        return annotationMirror.getAnnotationType().toString();
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public void setAnnotationMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
        this.annotationMirrorAtributes = new AnnotationMirrorAttributes();
    }

    public Iterable<AnnotationMirrorAtribute> getAnnotationMirrorAttributes() {
        return new AnnotationMirrorAttributes();
    }

    private class AnnotationMirrorAttributes implements Iterable<AnnotationMirrorAtribute>, Iterator<AnnotationMirrorAtribute> {
        private Iterator<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> iterator;

        private AnnotationMirrorAttributes() {
            iterator = annotationMirror.getElementValues().entrySet().iterator();
        }

        @Override
        public Iterator<AnnotationMirrorAtribute> iterator() {
            return AnnotationMirrorAttributes.this;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public AnnotationMirrorAtribute next() {
            AnnotationMirrorAtribute annotationMirrorAtribute = new AnnotationMirrorAtribute();
            Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> next = iterator.next();
            annotationMirrorAtribute.setKey(next.getKey().getSimpleName().toString());
            annotationMirrorAtribute.setValue(next.getValue().getValue().toString());
            return annotationMirrorAtribute;
        }

        @Override
        public void remove() {

        }
    }
}
