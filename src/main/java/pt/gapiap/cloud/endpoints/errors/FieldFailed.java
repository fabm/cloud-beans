package pt.gapiap.cloud.endpoints.errors;

import java.util.List;

public class FieldFailed {
    private String field;
    private List<Failure> failureList;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<Failure> getFailureList() {
        return failureList;
    }

    public void setFailureList(List<Failure> failureList) {
        this.failureList = failureList;
    }
}
