package pt.gapiap.cloud.endpoints.errors;

/**
 * Failure is container of vars to render an error
 */
public class Failure {
    private int code;
    private ErrorTemplate errorTemplate;
    private Object[] vars;

    public Failure(int code, ErrorTemplate errorTemplate, Object[] vars) {
        this.code = code;
        this.errorTemplate = errorTemplate;
        this.vars = vars;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ErrorTemplate getErrorTemplate() {
        return errorTemplate;
    }

    public void setErrorTemplate(ErrorTemplate errorTemplate) {
        this.errorTemplate = errorTemplate;
    }

    public Object[] getVars() {
        return vars;
    }

    public void setVars(Object[] vars) {
        this.vars = vars;
    }
    public String render(){
        return errorTemplate.render(vars);
    }
}
