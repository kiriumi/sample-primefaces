package application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domain.MailSender;
import domain.TwoStepVerificatior;
import dto.UserInfo;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import repository.UserInfoMapper;
import security.LoginManager;
import validation.constraints.AvailableChar;

@Named
@ViewScoped
public class Login extends BaseBackingBean {

    public static final String FLUSH_KEY_MESSAGE = "application.Login.flush.message";

    @Inject
    private LoginManager loginManager;

    private BCryptPasswordEncoder passwordEncoder;

    @NotBlank
    @AvailableChar
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

    @Getter
    @Setter
    private boolean overLoginLimit;

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

        this.succcess = false;
        this.user = null;

        return null;
    }

    private UserInfo user;
    private boolean succcess;

    @Transactional
    public void validate(ActionEvent event) {

        this.succcess = false;
        this.user = null;

        UserInfo selectedUser = mapper.selectByPrimaryKey(id);
        if (selectedUser == null) {
            messageService().addMessageError(List.of("id", "password"), "IDかパスワードが間違ってるよ");
            return;
        }

        this.user = selectedUser;

        boolean tempLocked = LocalDateTime.now().isBefore(user.getUnlockedDateTime());
        if (user.getLocked() || tempLocked) {
            messageService().addMessageError(List.of("id", "password"), "IDかパスワードが間違ってるよ");
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

                // 本来はここで、アカウントロックが掛かったことを、メールでユーザに通知する

                messageService().addMessageError(List.of("id", "password"), "IDかパスワードが間違ってるよ");
                return;
            }

            user.setFailCount(failCount);
            mapper.updateByPrimaryKeySelective(user);

            messageService().addMessageError(List.of("id", "password"), "IDかパスワードが間違ってるよ");

            return;
        }

        this.overLoginLimit = loginManager.overLoginLimit(id);

        this.succcess = true;
    }

    @Transactional
    public String login() {

        if (!succcess) {
            return null;
        }

        if (overLoginLimit) {
            loginManager.deleteOldestLoginUser(id);
        }

        user.setFailCount(0);
        mapper.updateByPrimaryKeySelective(user);

        setUser(user);
        loginManager.setup(user.getId(), autoLogin);

        twoStep.setup(user.getEmail(), "ログインに置ける本人確認", "/application/top", "login", () -> {
            try {
                MailSender.sendMail(user.getEmail(), "ログイン通知", "ログインされました");

            } catch (MessagingException e) {
                e.printStackTrace();
            }

            loginManager.login();
        });

        return redirect("twoStepVerification");
    }

    public String goPreSignup() {
        return redirect("preSignup");
    }

}
