package application;

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
        return null;
    }

}
