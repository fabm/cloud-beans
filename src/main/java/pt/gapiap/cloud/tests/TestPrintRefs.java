package pt.gapiap.cloud.tests;

import com.google.inject.Inject;
import pt.gapiap.proccess.annotations.ApiMethodParameters;
import pt.gapiap.proccess.logger.Logger;
import pt.gapiap.utils.TypeUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class TestPrintRefs {
    @Inject
    Logger logger;

    Set<DeclaredType> declaredTypeSet;
    List<DeclaredType> declaredTypesList;
    Set<TypeElement> typeElementSet;
    List<TypeElement> typeElementsList;

    public static TestPrintRefs create() {
        return new TestPrintRefs();
    }


    public void testMirros(RoundEnvironment env, Set<? extends TypeElement> annotations) {
        logger.setTrace(false);
        declaredTypeSet = new HashSet<>();
        typeElementSet = new HashSet<>();
        declaredTypesList = new ArrayList<>();
        typeElementsList = new ArrayList<>();
        StringBuilderWithIdentation sb = new StringBuilderWithIdentation(0);

        for (TypeElement te : annotations) {
            sb.addLineF("%-20s:%s", "annotation", te.getQualifiedName());

            logger.log(sb.toString());
            for (Element e : env.getElementsAnnotatedWith(te)) {
                printElement(1, e);
            }
        }
        logger.log("sets:\n");
        for (DeclaredType declaredType : declaredTypeSet) {
            logger.log("declared type:" + declaredType.toString() + "\n");
        }
        for (TypeElement typeElement : typeElementSet) {
            logger.log("type elements:" + typeElement + "\n");

        }
        logger.log("lists:\n");
        for (DeclaredType declaredType : declaredTypesList) {
            logger.log("declared type:" + declaredType.toString() + "\n");
        }
        for (TypeElement typeElement : typeElementsList) {
            logger.log("type elements:" + typeElement + "\n");
        }
    }

    private StringBuilderWithIdentation printSuper(int level, TypeElement typeElement) {
        StringBuilderWithIdentation sb = new StringBuilderWithIdentation((level + 1) * 4);

        TypeElement ts = TypeUtils.getFromTEtoSuperTE(typeElement);
        if (ts != null) {
            sb.addLineF("%-20s:%s", "super type:", ts.getQualifiedName());
            sb.append(printSuper(++level, ts));
        }
        return sb;
    }

    private StringBuilderWithIdentation printType(int level, TypeMirror typeMirror) {
        StringBuilderWithIdentation sb = new StringBuilderWithIdentation(level * 4);
        if (typeMirror.getKind() == TypeKind.DECLARED) {
            try {
                DeclaredType declaredType = Caster.castTo(DeclaredType.class, typeMirror, sb);
                sb.addLineF("%-20s:%s", "type arguments", declaredType.getTypeArguments().toString());
                sb.addLineF("%-20s:%s", "enclosing type", declaredType.getEnclosingType().toString());
            } catch (CastEvaluationException e) {
            }
        }
        return sb;
    }

    private StringBuilderWithIdentation printAnnotations(int level, Element element) {
        StringBuilderWithIdentation sb = new StringBuilderWithIdentation((level + 1) * 4);
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            sb.addLineF("%-20s:%s", "AnnotationMirror", annotationMirror.getAnnotationType().toString());
            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> entrySet = annotationMirror.getElementValues().entrySet();
            StringBuilderWithIdentation sbAM = new StringBuilderWithIdentation((level + 2) * 4);
            sb.addLineF("%-20s:%s", "ann. parameters", "values");
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : entrySet) {
                sbAM.addLineF("%-20s:%s", "key", "------------");
                sbAM.addLineF("%-20s:%s", "kind", entry.getKey().getKind());
                sbAM.addLineF("%-20s:%s", "default value", entry.getKey().getDefaultValue());
                sbAM.addLineF("%-20s:%s", "return type", entry.getKey().getReturnType());
                sbAM.addLineF("%-20s:%s", "kind return type", entry.getKey().getReturnType().getKind());
                sbAM.addLineF("%-20s:%s", "name", entry.getKey().getSimpleName());

                sbAM.addLineF("%-20s:%s", "value", "------------");
                Object value = entry.getValue().getValue();
                sbAM.addLineF("%-20s:%s", "value", value);
                StringBuilderWithIdentation sbTM = new StringBuilderWithIdentation((level + 3) * 4);
                if (value instanceof DeclaredType) {
                    sbAM.addLineF("%-20s:%s", "is typeMirror", "true");
                    DeclaredType declaredType = (DeclaredType) value;
                    declaredTypeSet.add(declaredType);
                    typeElementSet.add((TypeElement) declaredType.asElement());
                    declaredTypesList.add(declaredType);
                    typeElementsList.add((TypeElement) declaredType.asElement());
                    sbTM.addLineF("%-20s:%s", "kind", declaredType.getKind());
                    sbTM.addLineF("%-20s:%s", "element kind", declaredType.asElement().getKind());
                    sbTM.addLineF("%-20s:%s", "element class", declaredType.asElement());
                } else {
                    sbAM.addLineF("%-20s:%s", "is typeMirror", "false");
                }
                sbAM.append(sbTM);

            }
            sb.append(sbAM);
        }
        return sb;
    }

    private void printElement(int level, Element element) {
        StringBuilderWithIdentation sb = new StringBuilderWithIdentation(level * 4);

        sb.append(printSuper(0, (TypeElement) element));

        sb.addLineF("%-20s:%s", "kind", element.getKind());
        sb.addLineF("%-20s:%s", "name", element.getSimpleName());
        ApiMethodParameters annotation = element.getAnnotation(ApiMethodParameters.class);
        if (annotation != null) {
            sb.addLineF("%-20s:%s", "api", annotation.api());
            sb.addLineF("%-20s:%s", "method", annotation.method());
            try {
                sb.addLineF("%-20s:%s", "validator", annotation.validator().getSimpleName());
            } catch (MirroredTypeException e) {
                DeclaredType dt = (DeclaredType) e.getTypeMirror();
                declaredTypeSet.add(dt);
                declaredTypesList.add(dt);
                sb.addLineF("%-20s:%s", "error", dt);
            }
        }
        if (element.getKind() == ElementKind.CLASS) {
            TypeElement typeElement = null;
            try {
                typeElement = Caster.castTo(TypeElement.class, element, sb);
                sb.addLineF("%-20s:%s", "type", ":");
                sb.addLineF("%-20s:%s", "Qualified name", typeElement.getQualifiedName());
                sb.addLineF("%-20s:%s", "Super class", typeElement.getSuperclass());
                sb.addLineF("%-20s:%s", "Interfaces", typeElement.getInterfaces().toString());
                sb.append(printType(level + 1, typeElement.asType()));
            } catch (CastEvaluationException e) {
            }
        } else if (element.getKind() == ElementKind.FIELD) {
            try {
                VariableElement variableElement = Caster.castTo(VariableElement.class, element, sb);
            } catch (CastEvaluationException e) {
            }

        } else if (element.getKind() == ElementKind.METHOD) {
            try {
                ExecutableElement executableElement = Caster.castTo(ExecutableElement.class, element, sb);
            } catch (CastEvaluationException e) {

            }
        }
        sb.append(printAnnotations(level + 1, element));
        sb.lineOfRepeatedChars('-', 100);
        logger.log(sb.toString());
        if (level < 1) {
            for (Element inner : element.getEnclosedElements()) {
                printElement(level + 1, inner);
            }
        }
    }

}
