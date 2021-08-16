package application;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.TwoFactorAuther;
import domain.UserInfo;
import exception.WebApplicationException;
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
    private TwoFactorAuther twoFactor;

    @Inject
    private UserInfo user;

    public String init() {

        if (!twoFactor.isSetuped()) {
            throw new WebApplicationException();
        }
        return null;
    }

    public String verify() throws Exception {

        if (!twoFactor.verify(token)) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "トークンが違うよ");
            user.countupFail();
            return null;
        }

        flash().put(TwoFactorAuther.FLASH_TWO_FACTOR_AUTHED_KEY, true);

        return redirect(twoFactor.getRedirectPage());
    }

    public String resendToken() {
        twoFactor.sendTokenByMail();
        return null;
    }

    public String backPage() {
        return redirect(twoFactor.getBackPage());
    }
}
