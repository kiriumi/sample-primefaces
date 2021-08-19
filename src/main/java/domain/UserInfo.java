package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import lombok.Getter;
import lombok.Setter;

@SessionScoped
public class UserInfo implements Serializable {

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

	private LocalDateTime unlockedDatetime = LocalDateTime.MIN;

	private long lockedMinuteByLimitOver;

	private boolean locked;

	private boolean lockedByLimitOver;

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
			this.unlockedDatetime = LocalDateTime.now().plusMinutes(lockedMinuteByLimitOver);
			this.lockedByLimitOver = true;
		}
	}

	public boolean isLocked() {
		return locked || isLockedByLimitOver();
	}

	private boolean isLockedByLimitOver() {

		boolean locked = LocalDateTime.now().isBefore(unlockedDatetime);
		if (lockedByLimitOver == true && locked == false) {
			this.failCount = 0;
			this.lockedByLimitOver = false;
		}
		return locked;
	}

	public void resetFailCount() {
		this.failCount = 0;
	}

}
