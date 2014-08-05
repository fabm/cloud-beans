package pt.json.proccess.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provider;
import com.google.inject.Provides;

public class MyModule extends AbstractModule  {
    private BindedSingletoneClass bindedSingletoneClass;


    @Provides
    private BindedClass getBindedClass(){
        return new BindedClass();
    }

    @Provides
    private BindedSingletoneClass getBindedSingletone(){
        if (bindedSingletoneClass == null) {
            bindedSingletoneClass = new BindedSingletoneClass();
        }
        ++bindedSingletoneClass.timesInjected;
        return bindedSingletoneClass;
    }

    @Override
    protected void configure() {
    }

}
