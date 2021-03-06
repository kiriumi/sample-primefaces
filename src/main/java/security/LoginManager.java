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

import dto.LoginedUser;
import dto.LoginedUserExample;
import dto.LoginedUserExample.Criteria;
import lombok.Getter;
import repository.LoginedUserCustomMapper;
import repository.LoginedUserMapper;

@Named
@SessionScoped
public class LoginManager implements Serializable {

    private static final String SESSION_KEY_USERID = "security.LoginManager.userId";

    private static final String COOKIE_KEY_SESSION = "JSESSIONID";

    private static final int UNIT_DAY = 60 * 60 * 24;

    @Inject
    private ExternalContext extCtx;

    private boolean autoLogin;

    @Getter
    private boolean logined;

    private Cookie sessionCookie;

    private LocalDateTime autoLoginExpiresDateTime;

    @Inject
    private LoginedUserMapper mapper;

    @Inject
    private LoginedUserCustomMapper customMapper;

    public void setup(String userId, boolean autoLogin) {
        extCtx.getSessionMap().put(SESSION_KEY_USERID, userId); // ログ出力のstaticメソッドからユーザIDを取得できるようにするため
        this.autoLogin = autoLogin;
    }

    public void changeUserId(String userId) {
        extCtx.getSessionMap().put(SESSION_KEY_USERID, userId);
    }

    public static String getUserId() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .getOrDefault(SESSION_KEY_USERID, "");
    }

    public void login() {

        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();

        req.changeSessionId();

        LoginedUser loginedUser = new LoginedUser();
        loginedUser.setSessionId(extCtx.getSessionId(false));
        loginedUser.setId(getUserId());
        mapper.insertSelective(loginedUser);

        this.logined = true;
    }

    public void logout() {

        if (autoLogin) {
            // セッションクッキーを破棄
            Map<String, Object> cookieAttr = Map.of("path", extCtx.getRequestContextPath(), "maxAge", 0, "httpOnly",
                    true);
            extCtx.addResponseCookie(COOKIE_KEY_SESSION, extCtx.getSessionId(false), cookieAttr);
            this.autoLogin = false;
        }

        mapper.deleteByPrimaryKey(extCtx.getSessionId(false));
        extCtx.invalidateSession();

        this.logined = false;
    }

    public void deleteSessionId(String sessionId) {
        mapper.deleteByPrimaryKey(sessionId);
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

    public boolean hasUser() {

        LoginedUser loginedUser = new LoginedUser();
        loginedUser.setSessionId(extCtx.getSessionId(false));
        loginedUser.setUpdatedtime(LocalDateTime.now());
        long updateCount = mapper.updateByPrimaryKeySelective(loginedUser);

        if (updateCount != 1) {
            return false;
        }
        return true;
    }

    public boolean overLoginLimit(String id) {

        // 最大ログイン数を取得
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
        String strLimit = bundle.getString("login.user.same.id.limit");
        int limit = Integer.parseInt(strLimit);

        // 現在のログイン数数を取得
        LoginedUserExample example = new LoginedUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        long count = mapper.countByExample(example);

        return limit <= count;
    }

    public void deleteOldestLoginUser(String id) {
        customMapper.deleteOldestUser(id);
    }

}
