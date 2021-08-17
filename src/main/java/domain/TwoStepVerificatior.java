package domain;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class TwoStepVerificatior implements Serializable {

    public static final String FLASH_KEY_TWO_STEP_VERIFICATR_SUCCESS = "domain.TwoStepVerificatior.success";

    @Inject
    private ExternalContext extCtx;

    private String email;

    @Setter
    private String redirectPage;

    @Setter
    private String backPage;

    @Getter
    @Setter
    private String token;

    @Getter
    private boolean setuped;

    private Runnable callback;

    public void setup(String email, String redirectPage, String backPage) {
        clear();
        this.email = email;
        this.redirectPage = redirectPage;
        this.backPage = backPage;
        this.setuped = true;
        sendTokenByMail();
    }

    public void setup(String email, String redirectPage, String backPage, Runnable callbackIfSuccessed) {
        setup(email, redirectPage, backPage);
        this.callback = callbackIfSuccessed;
    }

    public void sendTokenByMail() {
        generateToken();
        sendMail();
    }

    public boolean verify(String inputedToken) {

        boolean result = Objects.equals(token, inputedToken);

        if (result) {
            extCtx.getFlash().put(TwoStepVerificatior.FLASH_KEY_TWO_STEP_VERIFICATR_SUCCESS, true);
            if (callback != null) {
                callback.run();
            }
        }
        return result;
    }

    public boolean isSuccessed() {
        return (boolean) extCtx.getFlash().getOrDefault(TwoStepVerificatior.FLASH_KEY_TWO_STEP_VERIFICATR_SUCCESS, false);
    }

    public String getRedirectPage() {
        String tmpRedirectPage = redirectPage;
        clear();
        return tmpRedirectPage;
    }

    public String getBackPage() {
        String tmpBackPage = backPage;
        clear();
        return tmpBackPage;
    }

    private void clear() {
        this.email = null;
        this.redirectPage = null;
        this.backPage = null;
        this.token = null;
    }

    private void generateToken() {
        this.token = "123456";
    }

    private void sendMail() {
        System.out.println(email);
    }

}
