package pt.json.proccess.test.guice;

import com.google.inject.Inject;
import org.junit.Assert;
import org.mockito.Mock;
import pt.gapiap.proccess.json.writer.AnProcWriters;
import pt.gapiap.proccess.logger.Logger;

import java.io.PrintWriter;

public class AnProcWritersTest implements AnProcWriters{

    private Logger logger;

    public AnProcWritersTest(Logger logger) {
        this.logger = logger;
        Assert.assertNotNull(logger);
    }

    @Override
    public PrintWriter getJsonWriter() {
        return new PrintWriter(System.out);
    }

    @Override
    public Logger getlogger() {
        return logger;
    }

    @Override
    public void close() {
    }
}
