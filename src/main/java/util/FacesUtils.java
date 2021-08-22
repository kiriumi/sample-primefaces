package util;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class FacesUtils {

    private FacesUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBackingBean(FacesContext facesContext) {

        ExternalContext extCtx = facesContext.getExternalContext();

        // トークンチェック対象の画面か判定
        ELContext elContext = facesContext.getELContext();
        ELResolver elResolver = elContext.getELResolver();

        HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
        String path = req.getRequestURL().toString();

        // ページ指定なしの場合、ウェルカムページのURLに変換
        String uri = req.getRequestURI();
        if (req.getContextPath().endsWith(uri.substring(0, uri.length() - 1))) {
            path = StringUtils.join(path, extCtx.getRequestServletPath().substring(1));
        }

        String viewId = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        String beanName = StringUtils.join(viewId.substring(0, 1).toLowerCase(), viewId.substring(1));
        return (T) elResolver.getValue(elContext, null, beanName);
    }

    public static UIComponent findComponent(final String id) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot root = facesContext.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        VisitContext visitContext = VisitContext.createVisitContext(facesContext);
        root.visitTree(visitContext, (visitCtx, component) -> {
            if (component != null
                    && id.equals(component.getId())) {
                found[0] = component;
                return VisitResult.COMPLETE;
            }
            return VisitResult.ACCEPT;
        });

        return found[0];

    }
}
