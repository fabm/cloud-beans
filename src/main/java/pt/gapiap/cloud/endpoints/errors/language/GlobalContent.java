package pt.gapiap.cloud.endpoints.errors.language;

import pt.gapiap.cloud.endpoints.errors.ErrorContent;

public interface GlobalContent extends ErrorContent {
    int NOT_AUTHORIZED = 0;
    int NOT_AUTHENTICATED = 1;
    int UNEXPECTED = 2;
}
