package application;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import domain.ChatPusher;
import dto.Chatter;
import dto.ChatterExample;
import dto.ChatterExample.Criteria;
import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import repository.ChatterMapper;

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
    private String recievedGroup;

    private String group;

    @Inject
    private ChatterMapper mapper;

    public String init() {

        this.group = (String) flash().get("application.Menubar.group");
        if (StringUtils.isBlank(group)) {
            return redirect("/application/top");
        }

        ChatterExample example = new ChatterExample();
        Criteria criteria = example.createCriteria();
        criteria.andGroupIdEqualTo(group);

        this.chatters = mapper.selectByExample(example);

        if (chatters == null) {
            this.chatters = new ArrayList<>();
        }

        return null;
    }

    public void send(ActionEvent event) {

        Chatter chatter = new Chatter();
        UserInfo user = getUser();

        chatter.setGroupId(group);
        chatter.setUserId(user.getId());
        chatter.setMessage(message);

        mapper.insertSelective(chatter);
        pusher.bloadchast(group);

        this.message = null;
    }

    public void recieve(ActionEvent event) {

        if (!group.equals(recievedGroup)) {
            return;
        }

        ChatterExample example = new ChatterExample();
        Criteria criteria = example.createCriteria();
        criteria.andGroupIdEqualTo(group);

        this.chatters = mapper.selectByExample(example);

        this.recievedGroup = null;
    }

}
