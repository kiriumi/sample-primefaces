package domain;

import lombok.Getter;
import lombok.Setter;

public class Chatter {

    @Getter
    private final String name;

    @Getter
    @Setter
    private String message;

    public Chatter(String name, String message) {
        this.name = name;
        this.message = message;
    }

}
