package event;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

public class SecurityHttpResponseHeaderListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(final PhaseEvent event) {

                final FacesContext facesContext = event.getFacesContext();
                final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

                invalidateChache(response);
                blockIframe(response);
                forceContentType(response);
                forceXssProtection(response);
    }

    public HttpServletResponse invalidateChache(final HttpServletResponse response) {
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-store");
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
