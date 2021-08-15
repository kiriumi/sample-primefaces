package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import domain.TwoFactor;
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
    private TwoFactor twoFactor;

    public String init() {

        if (loginManager.isTwoFactorAuthed()) {
            return  redirect("/application/top");
        }
        return null;
    }

    public String sendMail() {

        user.setEmail(email); // ユーザの仮登録

        twoFactor.sendTokenByMail(email);
        twoFactor.setRedirectPage("signup");

        return redirect("twoFactorAuth");
    }

}
