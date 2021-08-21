package application;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.TwoStepVerificatior;
import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import repository.UserInfoMapper;
import security.LoginManager;
import validation.constraints.AvirableChar;

@Named
@ViewScoped
public class Login extends BaseBackingBean {

    public static final String FLUSH_KEY_MESSAGE = "application.Login.flush.message";

    @Inject
    private LoginManager loginManager;

    private BCryptPasswordEncoder passwordEncoder;

    @NotBlank
    @AvirableChar
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean autoLogin;

    @Inject
    private UserInfoMapper mapper;

    @Inject
    private TwoStepVerificatior twoStep;

    private int failCountLimit;

    private long lockedMinuteByLimitOver;

    @PostConstruct
    public void onConstruct() {
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

        String strFailCountLimit = bundle.getString("login.fail.count.limit");
        this.failCountLimit = Integer.parseInt(strFailCountLimit);

        String strLockedMinute = bundle.getString("login.fail.limitover.locked.minutes");
        this.lockedMinuteByLimitOver = Long.parseLong(strLockedMinute);
    }

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }
        return null;
    }

    @Inject
    private UserTransaction tx;

    @Transactional
    public String login() {

        UserInfo user = mapper.selectByPrimaryKey(id);
        if (user == null) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            return null;
        }

        boolean tempLocked = LocalDateTime.now().isBefore(user.getUnlockedDateTime());
        if (user.getLocked() || tempLocked) {
            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
        }

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            int failCount = user.getFailCount();
            if (++failCount > failCountLimit) {

                LocalDateTime unlockedDatetime = LocalDateTime.now().plusMinutes(lockedMinuteByLimitOver);
                user.setUnlockedDateTime(unlockedDatetime);
                user.setFailCount(0);
                mapper.updateByPrimaryKeySelective(user);

                // 本来は、ここでアカウントロックが掛かったことを、メールでユーザに通知する

                messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
                return null;
            }

            user.setFailCount(failCount);
            mapper.updateByPrimaryKeySelective(user);

            messageService().addMessage(FacesMessage.SEVERITY_ERROR, "IDかパスワードが間違ってるよ");
            return null;
        }

        user.setFailCount(0);
        mapper.updateByPrimaryKeySelective(user);

        setUser(user);
        loginManager.setup(user.getId(), autoLogin);
        twoStep.setup(user.getEmail(), "/application/top", "login", () -> loginManager.login());

        return redirect("twoStepVerification");
    }

    public String goPreSignup() {
        return redirect("preSignup");
    }

}
