package token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import faces.BaseBackingBean;

public class TokenCheckListener implements PhaseListener {

    @Override
    public void beforePhase(final PhaseEvent event) {
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        verifyToken();
        updateToken();
    }

    private void verifyToken() {

        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = facesCtx.getExternalContext();

        // トークンチェック対象の画面か判定
        ELContext elContext = facesCtx.getELContext();
        ELResolver elResolver = elContext.getELResolver();

        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        String path = req.getRequestURL().toString();
        if (path.endsWith(req.getRequestURI())) {
            path = StringUtils.join(path, extCtx.getRequestServletPath().substring(1));
        }

        String viewId = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        String beanName = StringUtils.join(viewId.substring(0, 1).toLowerCase(), viewId.substring(1));
        BaseBackingBean bean = (BaseBackingBean) elResolver.getValue(elContext, null, beanName);

        TokenCheck tokenCheck = bean.getClass().getAnnotation(TokenCheck.class);
        if (tokenCheck == null) {
            //トークンチェック対象の画面ではない場合、何もしない
            return;
        }

        if (facesCtx.isPostback() || TokenUtils.isSamePage()) {
            // 初期表示処理以外の場合、何もしない
            return;
        }

        // セッションの取得
        Map<String, Object> session = extCtx.getSessionMap();
        @SuppressWarnings("unchecked")
        Map<String, String> childTokenMap = (Map<String, String>) session.get(TokenUtils.KEY_CHILD_TOKEN_MAP);
        if (childTokenMap == null) {
            childTokenMap = new HashMap<>();
            session.put("childTokenMap", childTokenMap);
        }

        // トークンチェックを実施
        if (TokenUtils.isParent()) {

            // 親画面の場合
            childTokenMap.clear(); // 子画面のトークンをリセット

            String tokenInSession = (String) session.get(TokenUtils.KEY_TOKEN);
            String tokenInRequest = extCtx.getRequestParameterMap().get(TokenUtils.KEY_TOKEN);

            if (tokenInSession == null || !tokenInSession.equals(tokenInRequest)) {
                // トークンチェック不正の場合
                session.remove(TokenUtils.KEY_TOKEN);
                throw new InvalidTokenException();
            }

        } else {

            // 子画面の場合
            String namespace = extCtx.getRequestParameterMap().get(TokenUtils.KEY_CHILD_TOKEN_NAMESPACE);
            String tokenInRequest = extCtx.getRequestParameterMap().get(TokenUtils.KEY_CHILD_TOKEN);

            if (tokenInRequest == null) {
                // 子画面を新規で開いた場合、親画面のトークンでチェック
                String parentTokenInSession = (String) session.get(TokenUtils.KEY_TOKEN);
                String parentTokenInRequest = extCtx.getRequestParameterMap().get(TokenUtils.KEY_TOKEN);

                if (!Objects.equals(parentTokenInSession, parentTokenInRequest)) {
                    throw new InvalidTokenException("トークンチェック不正");
                }
                return;
            }

            String tokenInSession = childTokenMap.get(namespace);

            if (tokenInSession == null || !tokenInSession.equals(tokenInRequest)) {
                childTokenMap.remove(namespace);
                throw new InvalidTokenException("トークンチェック不正");
            }
        }
    }

    private void updateToken() {

        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = facesCtx.getExternalContext();

        ELContext elContext = facesCtx.getELContext();
        ELResolver elResolver = elContext.getELResolver();

        Token token = (Token) elResolver.getValue(elContext, null, "token");
        ChildToken childToken = (ChildToken) elResolver.getValue(elContext, null, "childToken");

        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

        Map<String, Object> session = extCtx.getSessionMap();

        // 初期表示処理以外またはエラー画面の場合、トークンを画面に再設定
        if (facesCtx.isPostback() || TokenUtils.isSamePage()
                || extCtx.getRequestServletPath().endsWith(bundle.getString("error.page"))) {

            if (TokenUtils.isParent()) {
                // 親画面の場合
                String tokenInSession = (String) session.get(TokenUtils.KEY_TOKEN);
                if (StringUtils.isBlank(tokenInSession)) {
                    // 初回アクセスの場合
                    tokenInSession = generateToken();
                    session.put(TokenUtils.KEY_TOKEN, tokenInSession);
                }
                token.setToken(tokenInSession); // 画面にトークンを設定

            } else {
                // 子画面の場合
                String namespace = extCtx.getRequestParameterMap().get(TokenUtils.KEY_CHILD_TOKEN_NAMESPACE);

                @SuppressWarnings("unchecked")
                Map<String, String> childTokenMap = (Map<String, String>) session.get(TokenUtils.KEY_CHILD_TOKEN_MAP);
                String tokenInSession = childTokenMap.get(namespace); // セッションにトークンを設定

                // 画面にトークンを設定
                childToken.setNamespace(namespace);
                childToken.setToken(tokenInSession);
            }
            return;
        }

        // トークンを更新
        if (TokenUtils.isParent()) {
            // 親画面の場合
            String updatedToken = generateToken();
            session.put(TokenUtils.KEY_TOKEN, updatedToken); // セッションにトークンを設定
            token.setToken(updatedToken); // 画面にトークンを設定

        } else {
            // 子画面の場合
            String updatedToken = generateToken();
            String namespace = extCtx.getRequestParameterMap().get(TokenUtils.KEY_CHILD_TOKEN_NAMESPACE);

            @SuppressWarnings("unchecked")
            Map<String, String> childTokenMap = (Map<String, String>) session.get(TokenUtils.KEY_CHILD_TOKEN_MAP);
            childTokenMap.put(namespace, updatedToken); // セッションにトークンを設定

            // 画面にトークンを設定
            childToken.setNamespace(namespace);
            childToken.setToken(updatedToken);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private String generateToken() {
        long seed = new Random().nextLong() + new Date().getTime();
        return DigestUtils.sha256Hex(Long.toString(seed)).substring(0, 32);
    }

}
