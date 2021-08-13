package token;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.lang3.StringUtils;

import application.BaseBackingBean;

public class TokenCheckListener implements PhaseListener {

    @Override
    public void beforePhase(final PhaseEvent event) {
    }

    @Override
    public void afterPhase(final PhaseEvent event) {

        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = facesCtx.getExternalContext();

        // トークンチェック対象の画面か判定
        ELContext elContext = facesCtx.getELContext();
        ELResolver elResolver = elContext.getELResolver();

        String path = extCtx.getRequestServletPath();
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

                if (!parentTokenInSession.equals(parentTokenInRequest)) {
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

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
        //        return PhaseId.
    }

}
