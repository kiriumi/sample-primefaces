package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class ChildWindow2 extends BaseBackingBean {

    @Getter
    @Setter
    @NotBlank
    private String toParent;

    public String pass() {
        session().put("application.Top.passInfo", toParent);
        return null;
    }

    public String back() {
        return redirect("childWindow");
    }

    public String go() {
        return redirect("childWindow3");
    }
}
