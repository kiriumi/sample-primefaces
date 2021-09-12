package event;

import javax.inject.Inject;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import log.WebApplicationLogger;
import security.LoginManager;

public class WebSessionListener implements HttpSessionListener {

    @Inject
    LoginManager loginManager;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        try {
            loginManager.deleteSessionId(event.getSession().getId());

        } catch (Exception e) {
            WebApplicationLogger.INSTANCE.error(e, "error", e.getMessage());
        }

    }

}
