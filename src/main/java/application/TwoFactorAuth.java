package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.TwoFactor;
import domain.UserInfo;
import exception.AccountLokedException;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class TwoFactorAuth extends BaseBackingBean {

    @Getter
    @Setter
    private String token;

    @Inject
    private TwoFactor twoFactor;

    @Inject
    private UserInfo user;

    public String init() {

        if (!twoFactor.hasToken()) {
            return redirect("login");
        }

        if (user.isLocked()) {
            throw new AccountLokedException();
        }

        return null;
    }

    public String verify() throws Exception {

        if (user.isLocked()) {
            throw new AccountLokedException();
        }

        if (!twoFactor.valid(token)) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            user.countupFail();
            return null;
        }

        flash().put(TwoFactor.FLASH_TWO_FACTOR_AUTHED_KEY, true);

        return redirect(twoFactor.getRedirectPage());
    }
}
