package pt.gapiap.proccess.json.writer;

import com.google.inject.Inject;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;

public class AnProcWritersImpl implements AnProcWriters {

    private PrintWriter out;
    private Logger logger;

    public AnProcWritersImpl(Logger logger, ProcessingEnvironment processingEnvironment) {
        this.logger = logger;
        init(processingEnvironment);
    }

    private void init(ProcessingEnvironment processingEnv) {
        try {
            FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "validationMap.json");
            out = new PrintWriter(f.openWriter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrintWriter getJsonWriter() {
        return out;
    }

    @Override
    public Logger getlogger() {
        return logger;
    }

    @Override
    public void close() {
        logger.close();
        out.close();
    }


}
