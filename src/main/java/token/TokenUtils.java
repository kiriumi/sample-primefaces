package token;

import java.io.Serializable;

import javax.faces.context.FacesContext;

public class TokenUtils implements Serializable {

    public static final String KEY_TOKEN = "token";
    public static final String KEY_CHILD_TOKEN = "childToken";
    public static final String KEY_CHILD_TOKEN_NAMESPACE = "childTokenNamespace";
    public static final String KEY_CHILD_TOKEN_MAP = "childTokenMap";

    private TokenUtils() {
    }

    static boolean isParent() {
        return !FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .containsKey(KEY_CHILD_TOKEN_NAMESPACE);
    }

}
