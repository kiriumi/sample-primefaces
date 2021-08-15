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
public class TwoFactorAuth extends BaseBackingBean {

    @Getter
    @Setter
    private String token;

    public String check() {
        return redirect("/application/top.xhtml");
    }
}
