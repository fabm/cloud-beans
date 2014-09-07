package pt.gapiap.proccess.validation.bean.checker;

import java.util.Set;

interface Checker {

    void add(FailedField failedField);

    Set<FailedField> getFailedFields();
}
