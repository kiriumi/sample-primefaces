package domain;

import javax.enterprise.context.ApplicationScoped;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class UserInfo {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String password;
}
