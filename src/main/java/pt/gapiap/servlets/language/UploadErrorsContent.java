package pt.gapiap.servlets.language;


import pt.gapiap.cloud.endpoints.errors.ErrorContent;

public interface UploadErrorsContent extends ErrorContent {
    int NO_ACTION_PARAMETER = 0;
    int NO_UPLOAD_ACTION_REGISTERED=1;
    int KEY_NOT_NULL_STRING = 2;
    int KEY_ALREADY_EXISTS = 3;
}
