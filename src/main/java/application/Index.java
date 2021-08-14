package application;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import log.WebAccessLogging;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@WebAccessLogging
public class Index implements Serializable {

    @Getter
    @Setter
    private String text;

    public String init() {
        return null;
    }

    public String action() {
        return null;
    }
}
