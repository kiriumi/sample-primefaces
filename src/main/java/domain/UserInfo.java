package domain;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class UserInfo {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    private int failCount = 0;

    private int failCountLimit;

    private LocalDateTime lockedDatetimeByLimitOver = LocalDateTime.MIN;

    private long lockedMinuteByLimitOver;

    private boolean locked;

    @PostConstruct
    public void init() {
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

        String strFailCountLimit = bundle.getString("login.fail.count.limit");
        this.failCountLimit = Integer.parseInt(strFailCountLimit);

        String strLockedMinute = bundle.getString("login.fail.limitover.locked.minutes");
        this.lockedMinuteByLimitOver = Long.parseLong(strLockedMinute);
    }

    public void countupFail() {

        if (++failCount > failCountLimit) {
            this.lockedDatetimeByLimitOver = LocalDateTime.now().plusMinutes(lockedMinuteByLimitOver);
        }
    }

    public boolean isLocked() {
        return locked || isLockedByLimitOver();
    }

    private boolean isLockedByLimitOver() {
        return LocalDateTime.now().compareTo(lockedDatetimeByLimitOver) < 0;
    }

}
