package log;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import util.MessageUtils;

@Log4j2
public class WebApplicationLogger {

    public static void debug(final String message) {
        setLayout();
        log.debug(message);
        ThreadContext.clearAll();
    }

    public static void debug(final String id, final Object... params) {
        setLayout();
        log.debug(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public static void info(final String id, final Object... params) {
        setLayout();
        log.info(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public static void warn(final String id, final Object... params) {
        setLayout();
        log.warn(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public static void error(final String id, final Object... params) {
        setLayout();
        log.error(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    public static void error(Throwable throwable, final String id, final Object... params) {
        setLayout();
        log.error(MessageUtils.getMessage(id, params), throwable);
        ThreadContext.clearAll();
    }

    private static void setLayout() {
        LoggerUtils.putCalledClassAndMethod(WebApplicationLogger.class);
        LoggerUtils.putWebInfo();
    }

}
