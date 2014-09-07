package pt.gapiap.errors;

import pt.gapiap.cloud.endpoints.errors.FailTemplate;

public interface ErrorContents {
    FailTemplate[] getFailTemplates();
}
