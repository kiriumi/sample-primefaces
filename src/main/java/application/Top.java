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
public class Top extends BaseBackingBean {

    @Getter
    @Setter
    private String passInfo;

    public String showChildWindow() {
        session().put("application.Top.passInfo", "子画面に渡した情報");
        return null;
    }

    public String closedChildWindow() {
        this.passInfo = (String) session().get("application.Top.passInfo");
        return null;
    }

}
