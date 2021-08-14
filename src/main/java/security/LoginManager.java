package security;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

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
    private boolean autoLogin;

    public void login(String userId, boolean autoLogin) {

        if (logined) {
            return;
        }

        // ユーザIDをセッションから取得できるよう設定
        extCtx.getSessionMap().put(SESSION_KEY_USER_ID, StringUtils.defaultString(userId, ""));

        // セッションIDの変更
        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        req.changeSessionId();

        this.logined = true;

        if (autoLogin) {
            // 自動ログインの場合
            this.autoLogin = true;

            // 有効期限の取得
            ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
            String strAutoLoginInterval = bundle.getString("autologin.interval.day");
            int autoLoginInterval = Integer.parseInt(strAutoLoginInterval) * UNIT_DAY;

            extCtx.setSessionMaxInactiveInterval(autoLoginInterval); // セッションの有効期限を設定

            // セッションクッキーの有効期限を設定
            Map<String, Object> cookieAttr = Map.of("path", extCtx.getRequestContextPath(), "maxAge", autoLoginInterval, "httpOnly", true);
            extCtx.addResponseCookie(SESSION_COOKIE, extCtx.getSessionId(false), cookieAttr);
        }

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

        logined = false;
    }

    public boolean logined() {
        return logined;
    }

}
