package security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    public static final String SESSION_KEY_USER_ID = "security.LoginManager.userId";

    @Inject
    private ExternalContext extCtx;

    public static String getUserId() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SESSION_KEY_USER_ID);
    }

    private boolean logined;

    public void login(String userId) {

        if (logined) {
            return;
        }

        extCtx.getSessionMap().put(SESSION_KEY_USER_ID, StringUtils.defaultString(userId, ""));
        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        req.changeSessionId();
        this.logined = true;
    }

    public boolean logout() {
        extCtx.getSessionMap().remove(SESSION_KEY_USER_ID);
        extCtx.invalidateSession();
        return logined = false;
    }

    public boolean logined() {
        return logined;
    }

}
