package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    @Getter
    @Setter
    private String id;

    @Password
    @NotBlank
    @Getter
    @Setter
    private String password;

    @Inject
    private UserInfo user;

    @Inject
    private LoginManager loginManager;

    @Inject
    private TwoStepVerificatior twoStep;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }

        if (!twoStep.isSuccessed()) {
            return redirect("login");
        }
        return null;
    }

    public void validate() {
        return;
    }

    public String signup() {

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));

        messageService().addMessage(FacesMessage.SEVERITY_INFO, "ユーザ登録が完了したよ");
        flash().setKeepMessages(true);

        return redirect("login");
    }

}
