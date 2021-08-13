package jp.example;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class Index implements Serializable {

    @Getter
    @Setter
    private String text;

    public String action() {
        return null;
    }
}
