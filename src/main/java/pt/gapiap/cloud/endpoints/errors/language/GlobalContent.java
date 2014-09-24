package pt.gapiap.cloud.endpoints.errors.language;

import pt.gapiap.cloud.endpoints.errors.ErrorContent;

public interface GlobalContent extends ErrorContent {
    int NOT_AUTHORIZED = 401;
    int NOT_AUTHENTICATED = 403;
    int UNEXPECTED = 503;
}
