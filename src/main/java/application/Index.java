package application;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class Index extends BaseBackingBean {

    @Getter
    @Setter
    @NotBlank
    private String text;

    public String init() {
        return null;
    }

    public void actionListener(ActionEvent event) {
        return;
    }

    public String action() {
        return null;
    }

    public String go() {
        return redirect("index2");
    }

    @Getter
    @Setter
    @NotBlank
    private String toChild;

    @Getter
    @Setter
    private String fromChild;

    public String showChildWindow() {
        session().put("application.Top.passInfo", toChild);
        return null;
    }

    public String refresh() {
        this.fromChild = (String) session().get("application.Top.passInfo");
        session().remove("application.Top.passInfo");
        return null;
    }

}
