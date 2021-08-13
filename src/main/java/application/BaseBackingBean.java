package application;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.ApplicationLogger;
import lombok.Getter;
import security.LoginManager;
import token.ChildToken;
import token.Token;
import token.TokenUtils;

public class BaseBackingBean implements Serializable {

    @Inject
    @Getter
    private FacesContext facesContext;

    @Inject
    @Getter
    private ExternalContext externalContext;

    //    @Inject
    //    private MessageService messageService;

    @Inject
    private LoginManager loginManager;

    protected void logout() {
        loginManager.logout();
    }

    protected final HttpServletRequest request() {
        return (HttpServletRequest) externalContext.getRequest();
    }

    protected final HttpServletResponse response() {
        return (HttpServletResponse) externalContext.getResponse();
    }

    protected final Flash flash() {
        return externalContext.getFlash();
    }

    @Inject
    private ApplicationLogger logger;

    protected ApplicationLogger logger() {
        return logger;
    }

    //    protected MessageService messageService() {
    //        return messageService;
    //    }

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
