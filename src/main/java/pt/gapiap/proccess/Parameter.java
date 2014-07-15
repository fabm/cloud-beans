package pt.gapiap.proccess;

import pt.gapiap.proccess.hPrinter.HPrinter;
import pt.gapiap.proccess.hPrinter.Hprintable;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;

public class Parameter implements Hprintable {
    private VariableElement var;
    private boolean isUser;

    public String getName() {
        return name;
    }

    private String name;

    public Parameter(VariableElement element) {
        this.var = element;
        init();
    }

    public VariableElement getVar() {
        return var;
    }

    private void init() {
        name = getNameInit();
    }

    private String getNameInit() {
        List<? extends AnnotationMirror> annotationMirrors = var.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            String strAnnotation = annotationMirror.getAnnotationType().asElement().asType().toString();
            if (strAnnotation.equals("com.google.api.server.spi.config.Named")) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> value :
                        annotationMirror.getElementValues().entrySet()) {
                    if (value.getKey().equals("name")) return value.getValue().toString();
                }
            }
        }
        return null;
    }

    public boolean isUser() {
        return isUser;
    }

    @Override
    public void print(HPrinter hPrinter) {
        hPrinter.print("Parameter:");
        if (name != null)
            hPrinter.print("named:" + name + "\n");
        else hPrinter.print("\n");
    }
}
