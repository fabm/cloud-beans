package pt.test;

import com.google.inject.AbstractModule;
import pt.gapiap.proccess.logger.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestInjection extends AbstractModule{
    @Override
    protected void configure() {
        try {
            bind(TestInjected.class).toInstance(new TestInjected(new PrintWriter("x.txt")));
            bind(Logger.class).toInstance(new Logger(new PrintWriter("logger.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
