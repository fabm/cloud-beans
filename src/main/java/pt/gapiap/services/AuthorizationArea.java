package pt.gapiap.services;

public class AuthorizationArea<R extends Enum<R>> {
    private String area;

    public R[] getRoles() {
        return roles;
    }

    public void setRoles(R[] roles) {
        this.roles = roles;
    }

    private R[] roles;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}
