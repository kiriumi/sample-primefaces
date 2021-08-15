package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.UserInfo;
import faces.BaseBackingBean;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class Top extends BaseBackingBean {

    @Inject
    private UserInfo user;

    public String showUserInfo() {
        session().put("application.Top.flush.user" , user);
        return null;
    }
}
