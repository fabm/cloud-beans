package pt.gapiap.convert;


import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ModifierCv implements Conversor<Integer, Set<Modifier>> {
    int modifier;

    @Override
    public void setOriginal(Integer original) {
        modifier = original;
    }

    @Override
    public Set<Modifier> getConverted() {
        Set<Modifier> modifiers = new HashSet<>();
        if (java.lang.reflect.Modifier.isFinal(modifier)) {
            modifiers.add(Modifier.FINAL);
        }
        if (java.lang.reflect.Modifier.isNative(modifier)) {
            modifiers.add(Modifier.NATIVE);
        }
        if (java.lang.reflect.Modifier.isPrivate(modifier)) {
            modifiers.add(Modifier.PRIVATE);
        }
        if (java.lang.reflect.Modifier.isProtected(modifier)) {
            modifiers.add(Modifier.PROTECTED);
        }
        if (java.lang.reflect.Modifier.isPublic(modifier)) {
            modifiers.add(Modifier.PUBLIC);
        }
        if (java.lang.reflect.Modifier.isAbstract(modifier)) {
            modifiers.add(Modifier.ABSTRACT);
        }
        if (java.lang.reflect.Modifier.isStatic(modifier)) {
            modifiers.add(Modifier.STATIC);
        }
        if (java.lang.reflect.Modifier.isStrict(modifier)) {
            modifiers.add(Modifier.STRICTFP);
        }
        if (java.lang.reflect.Modifier.isSynchronized(modifier)) {
            modifiers.add(Modifier.SYNCHRONIZED);
        }
        throw new IllegalArgumentException("" + modifier);
    }
}
