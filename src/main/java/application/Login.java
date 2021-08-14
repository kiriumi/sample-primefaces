package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import faces.BaseBackingBean;
import log.WebApplicationLogger;
import lombok.Getter;
import lombok.Setter;
import security.LoginManager;

@Named
@ViewScoped
public class Login extends BaseBackingBean {

    @Inject
    private LoginManager loginManager;

    @Getter
    @Setter
    private String id;

    public String login() {

        loginManager.login(id);
        WebApplicationLogger.debug("ログインしたよ");

        return null;
    }
}
