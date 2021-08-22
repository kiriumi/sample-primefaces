package event;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import util.FacesUtils;

public class ErrorStyleListener implements PhaseListener {

    private static final String ATTR_STYLE_CLASS = "styleClass";
    private static final String CLASS_ERROR_STYLE = "ui-state-error";

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {

        FacesContext facesCtx = event.getFacesContext();

        // 前回のエラースタイルを削除
        getAllComponents(facesCtx.getViewRoot().getChildren()).forEach(component -> {

            String styleClass = String.valueOf(component.getAttributes().get(ATTR_STYLE_CLASS));
            if (styleClass != null && styleClass.indexOf(CLASS_ERROR_STYLE) >= 0) {
                component.getAttributes().put(ATTR_STYLE_CLASS, styleClass.replace(CLASS_ERROR_STYLE, "").trim());
            }
        });

        // エラーのある項目にエラースタイルを適用
        if (facesCtx.isValidationFailed()) {

            facesCtx.getClientIdsWithMessages().forEachRemaining(clientId -> {

                UIComponent component = FacesUtils.findComponent(clientId);
                String styleClassAttr = (String) component.getAttributes().get(ATTR_STYLE_CLASS);

                if (styleClassAttr != null) {
                    component.getAttributes().put(ATTR_STYLE_CLASS,
                            String.join(" ", styleClassAttr.trim(), CLASS_ERROR_STYLE));
                } else {
                    component.getAttributes().put(ATTR_STYLE_CLASS, CLASS_ERROR_STYLE);
                }

            });
        } else {
            // エラーなし
        }

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    private static Collection<UIComponent> getAllComponents(List<UIComponent> components) {

        Set<UIComponent> list = new HashSet<>();

        components.forEach(component -> {
            list.add(component);
            list.addAll(getAllComponents(component.getChildren()));
        });

        return list;
    }

}
