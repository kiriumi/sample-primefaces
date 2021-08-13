package application;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import security.LoginManager;

@Named
@ViewScoped
public class Login implements Serializable {

    @Inject
    private LoginManager loginManager;

    @Getter
    @Setter
    private String id;

    public String login() {
        loginManager.login();
        return null;
    }
}
