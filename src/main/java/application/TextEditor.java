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
public class TextEditor extends BaseBackingBean {

    @Getter
    @Setter
    private String text;

    public void action(ActionEvent event) {
        log().debug(text);
    }

}
