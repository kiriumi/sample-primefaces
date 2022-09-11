package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import faces.BaseBackingBean;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class ChildWindow3 extends BaseBackingBean {

    public String back() {
        return redirect("childWindow2");
    }

    public String go() {
        return redirect("childWindow");
    }
}
