package faces;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.WebAccessLogging;
import log.WebApplicationLogger;
import lombok.Getter;
import security.LoginManager;
import token.ChildToken;
import token.Token;
import token.TokenUtils;

@WebAccessLogging
public class BaseBackingBean implements Serializable {

    @Inject
    @Getter
    private FacesContext facesContext;

    @Inject
    @Getter
    private ExternalContext externalContext;

    protected HttpServletRequest request() {
        return (HttpServletRequest) externalContext.getRequest();
    }

    protected HttpServletResponse response() {
        return (HttpServletResponse) externalContext.getResponse();
    }

    protected Map<String, Object> session() {
        return externalContext.getSessionMap();
    }

    protected Flash flash() {
        return externalContext.getFlash();
    }

    protected WebApplicationLogger log;

    @Inject
    private MessageService messageService;

    protected MessageService messageService() {
        return messageService;
    }

    @Inject
    private LoginManager loginManager;

    protected String getUserId() {
        return LoginManager.getUserId();
    }

    protected void logout() {
        loginManager.logout();
    }

    @Inject
    private Token token;

    @Inject
    private ChildToken childToken;

    private static final String REDIRECT_PARAM = "?faces-redirect=true";

    protected String redirect(final String pageName) {

        if (pageName == null)
            return null;

        if (childToken.getNamespace() == null) {
            // 親画面での画面遷移の場合
            return String.join("", pageName, REDIRECT_PARAM, "&", TokenUtils.KEY_TOKEN, "=", token.getToken());
        }

        // 子画面での画面遷移の場合
        return String.join("", pageName, REDIRECT_PARAM,
                "&", TokenUtils.KEY_CHILD_TOKEN_NAMESPACE, "=", childToken.getNamespace(),
                "&", TokenUtils.KEY_CHILD_TOKEN, "=", childToken.getToken());
    }

}
