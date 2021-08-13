package log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseLogger {

    private final String logMessagePropBaseName = "LogMessages";

    public void debug(final String message) {
        log.debug(message);
    }

    public void debug(final String key, final Object... params) {
        log.debug(getLogMessage(key), params);
    }

    public void info(final String key, final Object... params) {
        log.info(getLogMessage(key), params);
    }

    public void warn(final String key, final Object... params) {
        log.warn(getLogMessage(key), params);
    }

    public void error(final String key, final Object... params) {
        log.error(getLogMessage(key), params);
    }

    private String getLogMessage(final String key) {

        putCalledClassAndMethod();
        putCustomInfo();

        ResourceBundle logMessageProp = ResourceBundle.getBundle(logMessagePropBaseName);
        String logMessage = null;

        try {
            logMessage = logMessageProp.getString(key);
        } catch (Exception e) {
            // 該当のキーが見つからない場合
            log.warn("該当のキーがないよ：{}", key);
        }

        return Optional.ofNullable(logMessage).orElse("");
    }

    private void putCalledClassAndMethod() {

        List<StackTraceElement> stackTraces = new ArrayList<>(Arrays.asList(Thread.currentThread().getStackTrace()));

        // ログ出力を呼び出したメソッドを探す
        int targetBackingBeanIndex = 0;
        for (StackTraceElement stackTrace : stackTraces) {
            if (stackTrace.getClassName()
                    .equals(Thread.class.getName()) || stackTrace.getClassName().equals(BaseLogger.class.getName())) {
                targetBackingBeanIndex++;
                continue;
            }
            break;
        }

        StackTraceElement stackTrace = stackTraces.get(targetBackingBeanIndex);

        ThreadContext.put("class", stackTrace.getClassName());
        ThreadContext.put("method", stackTrace.getMethodName());
    }

    protected void putCustomInfo() {
    };
}
