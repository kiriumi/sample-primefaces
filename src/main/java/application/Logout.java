package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import faces.BaseBackingBean;
import security.LoginManager;

@Named
@ViewScoped
public class Logout extends BaseBackingBean {

    @Inject
    private LoginManager loginManager;

    public String init() {

        if (!loginManager.isLogined()) {
            return redirect("login");
        }

        loginManager.logout();
        return null;
    }
}
