package pt.gapiap.proccess.validation.bean.checker;

import java.util.Map;

interface Checker {
    void check(CheckResult checkResult);
    Map<String, ?> getFailures();
}
