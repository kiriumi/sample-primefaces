package application;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import faces.BaseBackingBean;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class ChildWindow2 extends BaseBackingBean {
}
