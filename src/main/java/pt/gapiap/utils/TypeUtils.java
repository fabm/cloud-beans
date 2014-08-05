package pt.gapiap.utils;

import pt.gapiap.cloud.maps.ApiObject;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class TypeUtils {
    public static DeclaredType getFromDTtoSuperDT(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        TypeMirror superClass = typeElement.getSuperclass();
        if (superClass.getKind() == TypeKind.NONE) {
            return null;
        }
        return (DeclaredType) superClass;
    }

    public static TypeElement getFromTEtoSuperTE(TypeElement typeElement){
        DeclaredType dt = getFromDTtoSuperDT((DeclaredType) typeElement.asType());
        if(dt == null){
            return null;
        }
        return (TypeElement) dt.asElement();
    }
}
