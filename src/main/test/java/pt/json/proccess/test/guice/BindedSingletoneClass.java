package pt.json.proccess.test.guice;

public class BindedSingletoneClass {
    int timesInjected;

    @Override
    public String toString() {
        return "class injected "+ timesInjected+" times";
    }
}
