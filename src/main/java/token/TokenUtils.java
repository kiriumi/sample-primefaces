package token;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class TokenUtils implements Serializable {

    public static final String KEY_TOKEN = "token";
    public static final String KEY_CHILD_TOKEN = "childToken";
    public static final String KEY_CHILD_TOKEN_NAMESPACE = "childTokenNamespace";
    public static final String KEY_CHILD_TOKEN_MAP = "childTokenMap";

    private TokenUtils() {
    }

    public static boolean isParent() {
        return !FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .containsKey(KEY_CHILD_TOKEN_NAMESPACE);
    }

    public static boolean isSamePage() {

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();

        StringBuffer url = req.getRequestURL();
        String query = req.getQueryString();

        if (StringUtils.isNotBlank(query)) {
            url = req.getRequestURL().append("?").append(query);
        }

        String referer = StringUtils.defaultString(req.getHeader("Referer"));

        return url.toString().equals(referer);
    }
}
