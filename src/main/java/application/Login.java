package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.TwoFactor;
import domain.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import security.LoginManager;
import validation.constraints.AvirableChar;
import validation.constraints.Password;

@Named
@ViewScoped
public class Login extends BaseBackingBean {

    @Inject
    private LoginManager loginManager;

    private BCryptPasswordEncoder passwordEncoder;

    @AvirableChar
    @Getter
    @Setter
    private String id;

    @Password(message = "IDかパスワードが間違ってるよ")
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean autoLogin;

    @Inject
    private UserInfo user;

    @Inject
    private TwoFactor twoFactor;

    public String init() {

        if (user.isLocked()) {
            return redirect("accountLocked");
        }

        if (loginManager.isTwoFactorAuthed()) {
            return "top";
        }

        boolean twoFactorAuthed = (boolean) flash().getOrDefault(TwoFactor.FLASH_TWO_FACTOR_AUTHED_KEY, false);
        if (twoFactorAuthed) {
            loginManager.setTwoFactorAuthed(true);
            return "top";
        }

        return null;
    }

    public String login() {

        if (loginManager.logined()) {
            return null;
        }

        if (user.isLocked()) {
            return redirect("accountLocked");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            user.countupFail();
            return null;
        }

        loginManager.login(id, autoLogin);

        twoFactor.sendTokenByMail(user.getEmail());
        twoFactor.setRedirectPage("login");

        return redirect("twoFactorAuth");
    }

    public String goPreSignup() {
        return redirect("preSignup");
    }
}
