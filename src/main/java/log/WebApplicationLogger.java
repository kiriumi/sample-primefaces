package log;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import security.LoginManager;

@Dependent
public class WebApplicationLogger extends BaseLogger implements Serializable {

    @Inject
    private ExternalContext externalContext;

    @Inject
    private LoginManager loginManager;

    @Override
    protected void putCustomItems() {

        HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();

        ThreadContext.put("host", req.getLocalName());
        ThreadContext.put("url", req.getRequestURL().toString());
        ThreadContext.put("clientIp", getClientIp(req));
        ThreadContext.put("userId", loginManager.getUserId());
    }

    private String getClientIp(HttpServletRequest req) {
        String clientIp = req.getHeader("X-Forwarded-For");
        return StringUtils.isBlank(clientIp) ? req.getRemoteAddr() : clientIp;
    }

}
