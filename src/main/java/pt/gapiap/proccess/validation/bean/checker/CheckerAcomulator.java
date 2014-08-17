package pt.gapiap.proccess.validation.bean.checker;

import java.util.HashMap;
import java.util.Map;

public class CheckerAcomulator implements Checker{
    Map<String,Map<String,?>> map;

    public CheckerAcomulator() {
        map = new HashMap<>();
    }

    @Override
    public void check(CheckResult checkResult) {
        Map<String, ?> current = map.get(checkResult.getFieldName());
    }

    @Override
    public Map<String, ?> getFailures() {
        return null;
    }
}
