package pt.gapiap.cloud.endpoints.authorization;

import com.google.appengine.api.users.User;

import java.util.Objects;

public class UserServiceBean {
    private Class serviceClass;
    private User user;

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceClass, user.getEmail());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass().isAssignableFrom(UserServiceBean.class)) {
            return false;
        }
        UserServiceBean that = (UserServiceBean) obj;
        return that.serviceClass.equals(this.serviceClass) &&
                that.getUser().getEmail().equals(this.user.getEmail());
    }
}
