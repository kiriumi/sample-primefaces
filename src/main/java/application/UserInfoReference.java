package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class UserInfoReference extends BaseBackingBean {

    @Getter
    private UserInfo userInfo;

    public String init() {
        this.userInfo = getUser();
        return null;
    }

}
