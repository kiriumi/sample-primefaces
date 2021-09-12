package log;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import util.MessageUtils;

@Log4j2
public enum WebApplicationLogger {

    INSTANCE;

    public void debug(final String message) {
        setLayout();
        log.debug(message);
        ThreadContext.clearAll();
    }

    public void debug(final String id, final Object... params) {
        setLayout();
        log.debug(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public void info(final String id, final Object... params) {
        setLayout();
        log.info(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public void warn(final String id, final Object... params) {
        setLayout();
        log.warn(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public void error(final String id, final Object... params) {
        setLayout();
        log.error(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public void error(Throwable throwable, final String id, final Object... params) {
        setLayout();
        log.error(MessageUtils.getMessage(id, params), throwable);
        ThreadContext.clearAll();
    }

    private static void setLayout() {
        LoggerUtils.putCalledClassAndMethod(WebApplicationLogger.class);
        LoggerUtils.putWebInfo();
    }

}
