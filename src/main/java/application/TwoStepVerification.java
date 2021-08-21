package application;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.TwoStepVerificatior;
import exception.WebApplicationException;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class TwoStepVerification extends BaseBackingBean {

    @Getter
    @Setter
    private String token;

    @Inject
    private TwoStepVerificatior twoStep;

    private int failCount;

    private int failCountLimit;

    @PostConstruct
    public void onConstruct() {
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

        String strFailCountLimit = bundle.getString("login.fail.count.limit");
        this.failCountLimit = Integer.parseInt(strFailCountLimit);
    }

    public String init() {

        if (!twoStep.isSetuped()) {
            throw new WebApplicationException();
        }
        return null;
    }

    public String verify() throws Exception {

        if (!twoStep.verify(token)) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "トークンが違うよ");

            if (++failCount > failCountLimit) {
                throw new WebApplicationException("最初からやり直してください");
            }

            return null;
        }

        return redirect(twoStep.getRedirectPage());
    }

    public String resendToken() {
        twoStep.sendTokenByMail();
        return null;
    }

    public String backPage() {
        return redirect(twoStep.getBackPage());
    }

}
