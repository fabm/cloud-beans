package pt.gapiap.convert;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.util.List;

public class DeclaredTypeCv implements DeclaredType, Conversor<Class<?>, DeclaredType> {
    private Class<?> clazz;

    public DeclaredTypeCv(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public int hashCode() {
        return clazz.getCanonicalName().length();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeclaredTypeCv)) {
            return false;
        }
        DeclaredTypeCv dt = (DeclaredTypeCv) obj;
        return dt.clazz == clazz;
    }

    @Override
    public Element asElement() {
        return new TypeElementCv(clazz);
    }

    @Override
    public TypeMirror getEnclosingType() {
        return null;
    }

    @Override
    public List<? extends TypeMirror> getTypeArguments() {
        return null;
    }

    @Override
    public TypeKind getKind() {
        if(clazz == null){
            return TypeKind.NONE;
        }
        return TypeKind.DECLARED;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return null;
    }


    @Override
    public void setOriginal(Class<?> original) {
        this.clazz = original;
    }

    @Override
    public DeclaredType getConverted() {
        return this;
    }


    @Override
    public String toString() {
        return clazz.getCanonicalName();
    }
}
