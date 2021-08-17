package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.TwoStepVerificatior;
import domain.UserInfo;
import exception.AccountLokedException;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import security.LoginManager;
import validation.constraints.AvirableChar;

@Named
@ViewScoped
public class Login extends BaseBackingBean {

    public static final String FLUSH_KEY_MESSAGE = "application.Login.flush.message";

    @Inject
    private LoginManager loginManager;

    private BCryptPasswordEncoder passwordEncoder;

    @NotBlank
    @AvirableChar
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

    @Inject
    private TwoStepVerificatior twoStep;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }
        return null;
    }

    public String login() {

        // 本来、ここでDBからユーザ情報を取得
        if (user.isLocked()) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            return null;
        }

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.countupFail();
            if (user.isLocked()) {
                loginManager.setLocked(user.isLocked());
                throw new AccountLokedException();
            }

            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            return null;
        }

        user.resetFailCount();
        loginManager.setup(id, autoLogin);

        twoStep.setup(user.getEmail(), "/application/top", "login", () -> loginManager.login());

        return redirect("twoStepVerification");
    }

    public String goPreSignup() {
        return redirect("preSignup");
    }
}
