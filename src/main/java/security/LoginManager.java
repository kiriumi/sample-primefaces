package security;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    private static final String SESSION_KEY_USER_ID = "security.LoginManager.userId";

    private static final String SESSION_COOKIE = "JSESSIONID";

    private static final int UNIT_DAY = 60 * 60 * 24;

    @Inject
    private ExternalContext extCtx;

    public static String getUserId() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SESSION_KEY_USER_ID);
    }

    private boolean logined;

    @Getter
    private boolean twoFactorAuthed;

    private boolean autoLogin;

    private Cookie sessionCookie;

    private LocalDateTime autoLoginExpiresDateTime;

    public void login(String userId, boolean autoLogin) {

        // ユーザIDをセッションから取得できるよう設定
        extCtx.getSessionMap().put(SESSION_KEY_USER_ID, StringUtils.defaultString(userId, ""));

        // セッションIDの変更
        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        req.changeSessionId();

        this.logined = true;
        this.autoLogin = true;
    }

    public void authedTwoFactorAuthed() {
        this.twoFactorAuthed = true;
    }

    public void logout() {

        if (autoLogin) {
            // セッションクッキーを破棄
            Map<String, Object> cookieAttr = Map.of("path", extCtx.getRequestContextPath(), "maxAge", 0, "httpOnly", true);
            extCtx.addResponseCookie(SESSION_COOKIE, extCtx.getSessionId(false), cookieAttr);
            this.autoLogin = false;
        }

        extCtx.getSessionMap().remove(SESSION_KEY_USER_ID);
        extCtx.invalidateSession();

        this.logined = false;
        this.twoFactorAuthed = false;
    }

    public void activateAutoLogin() {

        if (!autoLogin || !twoFactorAuthed) {
            return;
        }

        HttpServletResponse res = (HttpServletResponse) extCtx.getResponse();

        if (sessionCookie != null) {
            // セッションクッキーの有効期限を設定し直し
            long autoLoginInterval = ChronoUnit.SECONDS.between(LocalDateTime.now(), autoLoginExpiresDateTime);
            autoLoginInterval = autoLoginInterval == 0 ? -1 : autoLoginInterval; // 有効期限が切れた場合、有効期限をセッションにする
            sessionCookie.setMaxAge((int) autoLoginInterval);
            res.addCookie(sessionCookie);
            return;
        }

        // 有効期限の取得
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
        String strAutoLoginInterval = bundle.getString("autologin.interval.day");
        int newAutoLoginInterval = Integer.parseInt(strAutoLoginInterval) * UNIT_DAY;

        this.autoLoginExpiresDateTime = LocalDateTime.now().plusSeconds(newAutoLoginInterval);

        extCtx.setSessionMaxInactiveInterval(newAutoLoginInterval); // セッションの有効期限を設定

        // セッションクッキーの有効期限を設定
        this.sessionCookie = new Cookie(SESSION_COOKIE, extCtx.getSessionId(false));
        sessionCookie.setPath(extCtx.getRequestContextPath());
        sessionCookie.setMaxAge(newAutoLoginInterval);
        sessionCookie.setHttpOnly(true);
        res.addCookie(sessionCookie);
    }

    public boolean logined() {
        return logined;
    }

}
