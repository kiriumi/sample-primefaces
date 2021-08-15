package domain;

import java.io.Serializable;
import java.util.Objects;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import lombok.Setter;

@Named
@SessionScoped
public class TwoFactor implements Serializable {

    public static final String FLASH_TWO_FACTOR_AUTHED_KEY = "domain.TwoFactor.authed";

    @Setter
    private String redirectPage;

    private String token;

    public String getRedirectPage() {

        String tmpRedirectPage = redirectPage;
        clear();

        return tmpRedirectPage;
    }

    public void sendTokenByMail(String emal) {
        this.token = "123456";
    }

    public boolean valid(String inputedToken) {
        return Objects.equals(token, inputedToken);
    }

    public boolean hasToken() {
        return StringUtils.isNoneBlank(token);
    }

    public void clear() {
        this.redirectPage = null;
        this.token = null;
    }
}
