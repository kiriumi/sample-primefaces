package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import faces.BaseBackingBean;
import log.WebAccessLogging;
import token.TokenCheck;

@Named
@ViewScoped
@WebAccessLogging
@TokenCheck
public class Index3 extends BaseBackingBean {

    public String init() {
        return null;
    }

    public String go() {
        return redirect("index");
    }
}
