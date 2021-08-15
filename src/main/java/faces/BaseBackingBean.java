package faces;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

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

    protected String getUserId() {
        return LoginManager.getUserId();
    }

    public String logout() {
        return redirect("/logout");
    }

    @Inject
    private Token token;

    @Inject
    private ChildToken childToken;

    private static final String PARAM_REDIRECT = "?faces-redirect=true";

    private static final String PARAM_KEY_VALUE_FORMAT = "&%s=%s";

    protected String redirect(final String pageName) {

        if (pageName == null)
            return null;

        if (childToken.getNamespace() == null) {
            // 親画面での画面遷移の場合

            return StringUtils.isBlank(token.getToken()) ? StringUtils.join(pageName, PARAM_REDIRECT) :
                StringUtils.join(pageName, PARAM_REDIRECT, String.format(PARAM_KEY_VALUE_FORMAT, TokenUtils.KEY_TOKEN, token.getToken()));
        }

        // 子画面での画面遷移の場合
        return StringUtils.join(pageName, PARAM_REDIRECT,
                String.format(PARAM_KEY_VALUE_FORMAT, TokenUtils.KEY_CHILD_TOKEN_NAMESPACE, childToken.getNamespace()),
                String.format(PARAM_KEY_VALUE_FORMAT, TokenUtils.KEY_CHILD_TOKEN, childToken.getToken()));
    }

}
