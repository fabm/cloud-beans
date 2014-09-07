package pt.json.proccess.test.guice;

import com.google.inject.Inject;

public class AnotherModuleTest {
    @Inject
    InnerClass innerClass;
    @Inject
    private BindedSingletoneClass bindedSingletoneClass;

    public BindedSingletoneClass getBindedSingletoneClass() {
        return bindedSingletoneClass;
    }

    public InnerClass getInnerClass() {
        return innerClass;
    }
}
