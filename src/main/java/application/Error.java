package application;

import java.util.ResourceBundle;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import faces.BaseBackingBean;

/**
 * システム障害画面
 * @author kengo
 *
 */
@Named
@ViewScoped
public class Error extends BaseBackingBean {

    public String init() {

        if (messageService().isEmpty()) {
            ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
            return redirect(bundle.getString("login.page"));
        }
        return null;
    }

}
