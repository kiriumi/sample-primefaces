package application;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import domain.TwoStepVerificatior;
import dto.UserInfo;
import dto.UserInfoExample;
import dto.UserInfoExample.Criteria;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import repository.UserInfoMapper;
import security.LoginManager;

@Named
@ViewScoped
public class PreSignup extends BaseBackingBean {

    @Email
    @NotBlank
    @Getter
    @Setter
    private String email;

    @Inject
    private LoginManager loginManager;

    @Inject
    private TwoStepVerificatior towStep;

    @Inject
    private UserInfoMapper mapper;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }
        return null;
    }

    public String sendMail() {

        UserInfoExample example = new UserInfoExample();
        Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        long count = mapper.countByExample(example);

        if (count != 0) {
            messageService().addMessageError("email", "そのメールアドレスは使用されています");
            return null;
        }

        UserInfo user = new UserInfo();
        user.setEmail(email);
        setUser(user);

        towStep.setup(email, "ユーザ登録における本人確認", "signup", "preSignup");

        return redirect("twoStepVerification");
    }

}
