package application;

import java.util.ResourceBundle;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import exception.WebExceptionHandler;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;

/**
 * システム障害画面
 * @author kengo
 *
 */
@Named
@ViewScoped
public class Error extends BaseBackingBean {

    @Getter
    @Setter
    private String errorMessage;

    public String init() {

        if (WebExceptionHandler.getException() == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
            return redirect(bundle.getString("login.page"));
        }

        this.errorMessage = WebExceptionHandler.getMessage();

        return null;
    }

}
