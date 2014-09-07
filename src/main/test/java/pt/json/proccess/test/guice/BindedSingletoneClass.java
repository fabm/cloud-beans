package pt.json.proccess.test.guice;

import com.google.inject.Inject;

public class BindedSingletoneClass {
    int timesInjected;
    @Inject
    InnerClass innerClass;

    @Override
    public String toString() {
        return "class injected "+ timesInjected+" times";
    }

    public InnerClass getInnerClass() {
        return innerClass;
    }
}
