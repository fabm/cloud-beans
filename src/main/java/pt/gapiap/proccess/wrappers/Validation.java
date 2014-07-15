package pt.gapiap.proccess.wrappers;

import pt.gapiap.proccess.mirrors.annotationMirror.AnnotationMirrorMapper;

import javax.lang.model.type.DeclaredType;

public class Validation {
    @AnnotationMirrorMapper("priority")
    int priority = 0;
    @AnnotationMirrorMapper("value")
    private DeclaredType declaredType;

    public DeclaredType getDeclaredType() {
        return declaredType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Validation)) {
            return false;
        }

        Validation par = (Validation) obj;
        boolean typesEqual = par.declaredType.toString().equals(this.declaredType.toString());

        if (typesEqual && this.priority != par.priority) {
            throw new RuntimeException("if types are equal priority must be equal");
        }

        return par.declaredType.toString().equals(this.declaredType.toString());
    }

    @Override
    public int hashCode() {
        return declaredType.toString().length();
    }

    @Override
    public String toString() {
        String out = "validation:" + declaredType + "\n";
        out += "priority:" + priority + "\n";
        return out;
    }
}
