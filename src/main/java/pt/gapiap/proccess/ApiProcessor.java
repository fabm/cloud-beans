package pt.gapiap.proccess;

import com.google.inject.Inject;
import com.google.inject.Injector;
import pt.gapiap.cloud.maps.ApiMapper;
import pt.gapiap.proccess.json.writer.AnProcWriters;
import pt.gapiap.proccess.json.writer.AnProcWritersImpl;
import pt.gapiap.proccess.logger.Logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class ApiProcessor implements ProcessorAction{
    @Inject
    Injector injector;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ApiMapper apiMapper = injector.getInstance(ApiMapper.class);
        apiMapper.init();
        AnProcWriters writers = injector.getInstance(AnProcWriters.class);
        writers.getJsonWriter().print(apiMapper.getJsonPreatifyApisMap());
        writers.close();
        return false;
    }
}
