package pt.gapiap.cloud.endpoints.errors.language;

import pt.gapiap.errors.ErrorContents;

public interface GlobalContents extends ErrorContents{
    int NOT_AUTHORIZED = 0;
    int NOT_AUTHENTICATED = 1;
    int UNEXPECTED = 2;
}
