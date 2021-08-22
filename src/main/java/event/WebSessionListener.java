package event;

import javax.inject.Inject;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import security.LoginManager;

public class WebSessionListener implements HttpSessionListener {

    @Inject
    LoginManager loginManager;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        loginManager.deleteSessionId(event.getSession().getId());
    }

}
