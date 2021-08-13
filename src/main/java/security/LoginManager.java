package security;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginManager {

    private boolean logined;

    public void login() {
        this.logined = true;
    }

    public boolean logined() {
        return logined;
    }

}
