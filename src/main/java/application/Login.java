package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.UserInfo;
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

    private BCryptPasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean autoLogin;

    @Inject
    private UserInfo user;

    public String signup() {

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        user.setId(id);

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        return null;
    }

    public String login() {

        if (loginManager.logined()) {
            return null;
        }

        if (user.isLocked() || user.isLockedByLimitOver()) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "アカウントがロックされてるよ");
            return null;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "パスワードが間違ってるよ");
            user.countupFail();
            return null;
        }

        loginManager.login(id, autoLogin);
        WebApplicationLogger.debug("ログインしたよ");

        return null;
    }
}
