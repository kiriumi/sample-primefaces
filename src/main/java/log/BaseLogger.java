package log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseLogger {

    public void debug(final String message) {

        putCalledClassAndMethod();
        putCustomItems();

        log.debug(message);
    }

    public void debug(final String key, final Object... params) {
        log.debug(getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public void info(final String key, final Object... params) {
        log.info(getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public void warn(final String key, final Object... params) {
        log.warn(getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public void error(final String key, final Object... params) {
        log.error(getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    private String getLogMessage(final String key) {

        putCalledClassAndMethod();
        putCustomItems();

        ResourceBundle bundle = ResourceBundle.getBundle(getBundleName());
        String logMessage = null;

        try {
            logMessage = bundle.getString(key);
        } catch (Exception e) {
            // 該当のキーが見つからない場合
            log.warn("該当のキーがないよ：{}", key);
        }

        return StringUtils.defaultString(logMessage, "");
    }

    protected void putCalledClassAndMethod() {

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

    protected void putCustomItems() {
    };

    protected abstract String getBundleName() ;
}
