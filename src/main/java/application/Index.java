package application;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import faces.BaseBackingBean;
import log.WebAccessLogging;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@WebAccessLogging
public class Index extends BaseBackingBean {

    @Getter
    @Setter
    private int output;

    @NotNull
    @Getter
    @Setter
    private int input;

    public String init() {
        return null;
    }

    public void actionListener(ActionEvent event) {
        return;
    }

    public String action() {
        this.output += input;
        return null;
    }

    public String action2() {
        return null;
    }
}
