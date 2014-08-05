package pt.json.proccess.test.guice;

import com.google.inject.Inject;

public class AnotherModuleTest {
    @Inject
    private BindedSingletoneClass bindedSingletoneClass;

    public BindedSingletoneClass getBindedSingletoneClass() {
        return bindedSingletoneClass;
    }
}
