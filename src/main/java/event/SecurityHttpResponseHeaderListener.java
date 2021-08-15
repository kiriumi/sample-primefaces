package event;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import security.LoginManager;

public class SecurityHttpResponseHeaderListener implements PhaseListener {

    @Inject
    private LoginManager loginManager;

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(final PhaseEvent event) {

        final FacesContext facesContext = event.getFacesContext();
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        invalidateCache(response);
        blockIframe(response);
        forceContentType(response);
        forceXssProtection(response);
        loginManager.activateAutoLogin();
    }

    public HttpServletResponse invalidateCache(final HttpServletResponse response) {
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
        return response;
    }

    public HttpServletResponse blockIframe(final HttpServletResponse response) {
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        return response;
    }

    public HttpServletResponse forceContentType(final HttpServletResponse response) {
        response.addHeader("X-Content-Type-Options", "nosniff");
        return response;
    }

    public HttpServletResponse forceXssProtection(final HttpServletResponse response) {
        response.addHeader("X-XSS-Protection", "1; mode=block");
        return response;
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
