package pt.gapiap.cloud.endpoints.errors;

public class Failure {
    private int code;
    private FailTemplate failTemplate;
    private Object[] vars;

    public Failure(int code, FailTemplate failTemplate, Object[] vars) {
        this.code = code;
        this.failTemplate = failTemplate;
        this.vars = vars;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public FailTemplate getFailTemplate() {
        return failTemplate;
    }

    public void setFailTemplate(FailTemplate failTemplate) {
        this.failTemplate = failTemplate;
    }

    public Object[] getVars() {
        return vars;
    }

    public void setVars(Object[] vars) {
        this.vars = vars;
    }
    public String render(){
        return failTemplate.render(vars);
    }
}
