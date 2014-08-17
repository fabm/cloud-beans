package pt.gapiap.proccess.validation.bean.checker;

import java.util.Map;

public class SimpleChecker implements Checker {
    private Map<String,?> failure;

    @Override
    public void check(CheckResult checkResult) {
        BeanCheckerException beanCheckerException = new BeanCheckerException();
        //todo ver onde posso por aqui o failure
        //beanCheckerException.validationContext = validationContext;
        throw beanCheckerException;
    }

    @Override
    public Map<String, ?> getFailures() {
        return failure;
    }

}
