package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import domain.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import token.TokenCheck;

@Named
@ViewScoped
@TokenCheck
public class UserInfoReference extends BaseBackingBean {

	@Inject
	@Getter
	@Setter
	private UserInfo user;

}
