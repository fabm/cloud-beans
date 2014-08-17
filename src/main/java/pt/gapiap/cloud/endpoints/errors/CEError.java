package pt.gapiap.cloud.endpoints.errors;


import pt.gapiap.cloud.endpoints.CEErrorIdentifier;

public abstract class CEError extends Exception {
    private CEErrorIdentifier ceErrorIdentifier;

    protected CEError() {
    }

    public CEError(CEErrorIdentifier ceErrorIdentifier) {
        this.ceErrorIdentifier = ceErrorIdentifier;
    }


    public CEErrorIdentifier getCeErrorIdentifier() {
        return ceErrorIdentifier;
    }
}
