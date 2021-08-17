package application;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import faces.BaseBackingBean;

@Named
@SessionScoped
public class Menubar extends BaseBackingBean{

    public String goTop() {
        return redirect("/application/top");
    }

    public String goUserInfoReference() {
        return redirect("/application/userInfoReference");
    }

    public String goFileload() {
        return redirect("/application/fileload");
    }

}
