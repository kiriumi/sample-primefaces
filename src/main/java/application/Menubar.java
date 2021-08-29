package application;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;

@Named
@SessionScoped
public class Menubar extends BaseBackingBean {

    @Getter
    String userId;

    @PostConstruct
    public void onConstruct() {
        UserInfo user = getUser();
        this.userId = user.getId();
    }

    public String goTop() {
        return redirect("/application/top");
    }

    public String goUserInfoReference() {
        return redirect("/application/userInfoReference");
    }

    public String goChat() {
        return redirect("/application/chat");
    }

    public String goFileload() {
        return redirect("/application/fileload");
    }

}
