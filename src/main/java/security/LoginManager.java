package security;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    private static final String COOKIE_KEY_SESSION = "JSESSIONID";

    private static final int UNIT_DAY = 60 * 60 * 24;

    @Inject
    private ExternalContext extCtx;

    @Getter
    private String userId;

    private boolean autoLogin;

    @Getter
    private boolean logined;

    private Cookie sessionCookie;

    private LocalDateTime autoLoginExpiresDateTime;

    public void setup(String userId, boolean autoLogin) {
        this.userId = userId;
        this.autoLogin = true;
    }

    public void login() {
        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        req.changeSessionId();
        this.logined = true;
    }

    public void logout() {

        if (autoLogin) {
            // セッションクッキーを破棄
            Map<String, Object> cookieAttr = Map.of("path", extCtx.getRequestContextPath(), "maxAge", 0, "httpOnly", true);
            extCtx.addResponseCookie(COOKIE_KEY_SESSION, extCtx.getSessionId(false), cookieAttr);
            this.autoLogin = false;
        }

        extCtx.getSessionMap().remove(COOKIE_KEY_SESSION);
        extCtx.invalidateSession();

        this.logined = false;
    }

    public void activateAutoLogin() {

        if (!autoLogin || !logined) {
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
        this.sessionCookie = new Cookie(COOKIE_KEY_SESSION, extCtx.getSessionId(false));
        sessionCookie.setPath(extCtx.getRequestContextPath());
        sessionCookie.setMaxAge(newAutoLoginInterval);
        sessionCookie.setHttpOnly(true);
        res.addCookie(sessionCookie);
    }

}
