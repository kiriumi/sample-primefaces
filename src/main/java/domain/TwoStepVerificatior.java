package domain;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class TwoStepVerificatior implements Serializable {

    public static final String FLASH_TWO_FACTOR_AUTHED_KEY = "domain.TwoFactor.authed";

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

    public void setup(String email, String redirectPage, String backPage) {
        clear();
        this.email = email;
        this.redirectPage = redirectPage;
        this.backPage = backPage;
        this.setuped = true;
        sendTokenByMail();
    }

    public void sendTokenByMail() {
        generateToken();
        sendMail();
    }

    public boolean verify(String inputedToken) {
        return Objects.equals(token, inputedToken);
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
