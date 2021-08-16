package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.TwoStepVerificatior;
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
            return  redirect("/application/top");
        }

        boolean twoFactorAuthed = (boolean) flash().getOrDefault(TwoStepVerificatior.FLASH_TWO_FACTOR_AUTHED_KEY, false);
        if (!twoFactorAuthed) {
            return  redirect("login");
        }
        return null;
    }

    public String signup() {

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));

        flash().put(Login.FLUSH_KEY_MESSAGE, "ユーザ登録が完了したよ");

        return  redirect("login");
    }

}
