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
public class ChildWindow extends BaseBackingBean {

    @Getter
    @Setter
    private String fromParent;

    @Getter
    @Setter
    @NotBlank
    private String toParent;

    public String init() {
        this.fromParent = (String) session().get("application.Top.passInfo");
        session().remove("application.Top.passInfo");
        return null;
    }

    public String pass() {
        session().put("application.Top.passInfo", toParent);
        return null;
    }

    public String go() {
        return redirect("childWindow2");
    }

}
