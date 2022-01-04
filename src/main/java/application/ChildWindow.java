package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

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
    private String passInfo;

    public String init() {
        this.passInfo = (String) session().get("application.Top.passInfo");
        return null;
    }

    public String close() {
        session().put("application.Top.passInfo", "親画面に渡した情報");
        return null;
    }

    public String goChildWindow2() {
        return redirect("/application/childWindow2.xhtml");
    }

}
