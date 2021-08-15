package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import domain.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class UserInfoReferrer extends BaseBackingBean {

    @Getter
    @Setter
    private UserInfo user;

    public String init() {
        this.user = (UserInfo) session().get("application.Top.flush.user");
        return null;
    }

    public String close() {
        session().remove("application.Top.flush.user");
        return null;
    }
}
