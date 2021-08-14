package security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    @Inject
    private ExternalContext extCtx;

    @Getter
    private String userId;

    private boolean logined;

    public void login(String userId) {

        if (logined) {
            return;
        }

        this.userId = StringUtils.defaultString(userId, "");

        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        req.changeSessionId();
        this.logined = true;
    }

    public boolean logout() {
        extCtx.invalidateSession();
        return logined = false;
    }

    public boolean logined() {
        return logined;
    }

}
