package application;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.ChatPusher;
import domain.Chatter;
import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class Chat extends BaseBackingBean {

    @Getter
    @Setter
    private List<Chatter> chatters = new ArrayList<>();

    @Inject
    private ChatPusher pusher;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String recieveName;

    @Getter
    @Setter
    private String recieveMessage;

    public String init() {

        chatters.add(new Chatter("太郎", "ほげ"));
        chatters.add(new Chatter("花子", "ふー"));

        return null;
    }

    public void send(ActionEvent event) {
        UserInfo user = getUser();
        //        pusher.bloadchast(new Chatter(user.getId(), message));
        pusher.bloadchast(new Chatter(user.getId(), message), "1111", "1112");
    }

    public void recieve(ActionEvent event) {

        chatters.add(new Chatter(recieveName, recieveMessage));

        this.message = null;
        this.recieveName = null;
        this.recieveMessage = null;
    }

}
