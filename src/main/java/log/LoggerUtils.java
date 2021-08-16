package log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import security.LoginManager;

@Log4j2
public abstract class LoggerUtils {

    private final static String bundleName = "LogMessages";
    private final static String bundleNameApplication = "LogMessagesApplication";

    private LoggerUtils() {
    }

    static String getLogMessage(final String key) {

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        String logMessage = null;

        try {
            logMessage = bundle.getString(key);

            if (StringUtils.isBlank(logMessage)) {
                bundle = ResourceBundle.getBundle(bundleNameApplication);
                logMessage = bundle.getString(key);
            }

        } catch (Exception e) {
            // 該当のキーが見つからない場合
            log.warn("該当のキーがないよ：{}", key);
        }

        return StringUtils.defaultString(logMessage, "");
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

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

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
