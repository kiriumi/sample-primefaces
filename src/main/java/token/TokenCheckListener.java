package token;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import faces.BaseBackingBean;
import util.FacesUtils;

public class TokenCheckListener implements PhaseListener {

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        verifyToken(event);
        updateToken(event);
    }

    private String generateToken() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    private void verifyToken(PhaseEvent event) {

        FacesContext facesCtx = event.getFacesContext();
        ExternalContext extCtx = facesCtx.getExternalContext();

        BaseBackingBean bean = FacesUtils.getBackingBean(facesCtx);
        TokenCheck tokenCheck = bean.getClass().getAnnotation(TokenCheck.class);
        if (tokenCheck == null || facesCtx.isPostback()) {
            //トークンチェック対象外の画面または、アクションの場合、何もしない
            return;
        }

        // 子画面トークンマップを取得
        Map<String, Object> session = extCtx.getSessionMap();
        @SuppressWarnings("unchecked")
        Map<String, String> childTokenMap = (Map<String, String>) session.get(TokenUtils.KEY_CHILD_TOKEN_MAP);
        if (childTokenMap == null) {
            // 子画面トークンマップの初期化
            childTokenMap = new HashMap<>();
            session.put("childTokenMap", childTokenMap);
        }

        // トークンチェックを実施
        if (TokenUtils.isParent()) {

            // 親画面の場合
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

    private void updateToken(PhaseEvent event) {

        FacesContext facesCtx = event.getFacesContext();
        ExternalContext extCtx = facesCtx.getExternalContext();

        ELContext elContext = facesCtx.getELContext();
        ELResolver elResolver = elContext.getELResolver();

        Token token = (Token) elResolver.getValue(elContext, null, "token");
        ChildToken childToken = (ChildToken) elResolver.getValue(elContext, null, "childToken");

        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");

        Map<String, Object> session = extCtx.getSessionMap();

        // アクションの場合、トークンを画面に再設定
        if (facesCtx.isPostback()) {

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

}
