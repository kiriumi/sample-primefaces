package security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    private boolean logined;

    public void login() {

        if (logined) {
            return;
        }

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        req.changeSessionId();
        this.logined = true;
    }

    public boolean logout() {
        ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
        extCtx.invalidateSession();
        return logined = false;
    }

    public boolean logined() {
        return logined;
    }

}
