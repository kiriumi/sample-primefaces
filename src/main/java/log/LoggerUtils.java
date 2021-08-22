package log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import security.LoginManager;

public abstract class LoggerUtils {

    private final static String bundleName = "Messages";

    private LoggerUtils() {
    }

    static void putCalledClassAndMethod(Class<?> loggerClass) {

        List<StackTraceElement> stackTraces = new ArrayList<>(Arrays.asList(Thread.currentThread().getStackTrace()));

        // ログ出力を呼び出したメソッドを探す
        int targetClassIndex = 0;
        for (StackTraceElement stackTrace : stackTraces) {
            if (stackTrace.getClassName().equals(Thread.class.getName())
                    || stackTrace.getClassName().equals(LoggerUtils.class.getName())
                    || stackTrace.getClassName().equals(loggerClass.getName())) {
                targetClassIndex++;
                continue;
            }
            break;
        }

        StackTraceElement stackTrace = stackTraces.get(targetClassIndex);
        ThreadContext.put("class", stackTrace.getClassName());
        ThreadContext.put("method", stackTrace.getMethodName());
    }

    static void putWebInfo() {

        FacesContext facesCtx = FacesContext.getCurrentInstance();

        if (facesCtx == null) {
            // APサーバ停止後のセッション破棄時のログ出力で、facesCtxがnullになるため
            return;
        }

        HttpServletRequest req = (HttpServletRequest) facesCtx.getExternalContext().getRequest();

        if (req == null) {
            return;
        }

        ThreadContext.put("host", req.getLocalName());
        ThreadContext.put("url", req.getRequestURL().toString());
        ThreadContext.put("clientIp", getClientIp(req));
        ThreadContext.put("userId", LoginManager.getUserId());
    }

    private static String getClientIp(HttpServletRequest req) {
        String clientIp = req.getHeader("X-Forwarded-For");
        return StringUtils.isBlank(clientIp) ? req.getRemoteAddr() : clientIp;
    }

}
