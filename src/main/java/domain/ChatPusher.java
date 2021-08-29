package domain;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ChatPusher {

    @Inject
    @Push
    private PushContext chatChannel;

    public void bloadchast(String group) {
        chatChannel.send(group);
    }

}
