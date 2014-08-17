package pt.gapiap.proccess.inject;

import javax.annotation.processing.RoundEnvironment;

public interface EnvironmentRun {
    void setRoundEnvironment(RoundEnvironment roundEnvironment);
}
