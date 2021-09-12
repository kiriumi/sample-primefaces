package application;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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
    private String text;

    public String init() {
        return null;
    }

    public void action(ActionEvent event) {
        System.out.println(text);
    }

}
