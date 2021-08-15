package application;

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
public class Signup extends BaseBackingBean {

    private BCryptPasswordEncoder passwordEncoder;

    @AvirableChar
    @Getter
    @Setter
    private String id;

    @Password
    @Getter
    @Setter
    private String password;

    @Inject
    private UserInfo user;

    @Inject
    private LoginManager loginManager;

    public String init() {

        if (loginManager.isTwoFactorAuthed()) {
            return "top";
        }

        boolean twoFactorAuthed = (boolean) flash().getOrDefault(TwoFactor.FLASH_TWO_FACTOR_AUTHED_KEY, false);
        if (!twoFactorAuthed) {
            return "login";
        }
        return null;
    }

    public String signup() {

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));

        return "login";
    }

}
