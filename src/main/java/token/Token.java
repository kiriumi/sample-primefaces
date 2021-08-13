package token;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class Token implements Serializable {

    @Getter
    @Setter
    private String token;
}
