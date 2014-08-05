package pt.json.proccess.test.guice;

import com.google.inject.Inject;

public class ModuleTest {
    @Inject
    BindedClass bindedClass;

    @Inject
    BindedSingletoneClass bindedSingletoneClass;

    public BindedClass getBindedClass() {
        return bindedClass;
    }

    public BindedSingletoneClass getBindedSingletoneClass() {
        return bindedSingletoneClass;
    }
}
