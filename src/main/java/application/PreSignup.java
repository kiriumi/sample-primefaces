package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import domain.TwoStepVerificatior;
import domain.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import security.LoginManager;

@Named
@ViewScoped
public class PreSignup extends BaseBackingBean {

    @Email
    @NotBlank
    @Getter
    @Setter
    private String email;

    @Inject
    private UserInfo user;

    @Inject
    private LoginManager loginManager;

    @Inject
    private TwoStepVerificatior towStep;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }
        return null;
    }

    public String sendMail() {

        user.setEmail(email); // ユーザの仮登録
        towStep.setup(email, "signup", "preSignup");

        return redirect("twoStepVerification");
    }

}
