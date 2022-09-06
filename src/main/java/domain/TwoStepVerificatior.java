package domain;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;

import exception.WebApplicationException;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class TwoStepVerificatior implements Serializable {

    public static final String FLASH_KEY_TWO_STEP_VERIFICATR_SUCCESS = "domain.TwoStepVerificatior.success";

    private static final String EMAIL_MASSAGE_FORMAT = "トークンです。\n%s";

    @Inject
    private ExternalContext extCtx;

    private String emailAddress;

    private String emailTitle;

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

    public void setup(String emailAddress, String emailTitle, String redirectPage, String backPage) {
        clear();
        this.emailAddress = emailAddress;
        this.emailTitle = emailTitle;
        this.redirectPage = redirectPage;
        this.backPage = backPage;
        this.setuped = true;
        sendTokenByMail();
    }

    public void setup(String emailAddress, String emailTitle,
            String redirectPage, String backPage, Runnable callbackIfSuccessed) {

        setup(emailAddress, emailTitle, redirectPage, backPage);
        this.callback = callbackIfSuccessed;
    }

    public void sendTokenByMail() {

        this.token = RandomStringUtils.randomNumeric(6);

        try {
            MailSender.sendMail(emailAddress, emailTitle,
                    String.format(EMAIL_MASSAGE_FORMAT, token));

        } catch (MessagingException e) {
            throw new WebApplicationException("Eメールの送信に失敗しました。", e);
        }
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
        return (boolean) extCtx.getFlash().getOrDefault(
                TwoStepVerificatior.FLASH_KEY_TWO_STEP_VERIFICATR_SUCCESS, false);
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
        this.emailAddress = null;
        this.redirectPage = null;
        this.backPage = null;
        this.token = null;
    }

}
