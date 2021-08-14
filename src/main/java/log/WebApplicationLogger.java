package log;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WebApplicationLogger {

    public static void debug(final String message) {
        setLayout();
        log.debug(message);
        ThreadContext.clearAll();
    }

    public static void debug(final String key, final Object... params) {
        setLayout();
        log.debug(LoggerUtils.getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public static void info(final String key, final Object... params) {
        setLayout();
        log.info(LoggerUtils.getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public static void warn(final String key, final Object... params) {
        setLayout();
        log.warn(LoggerUtils.getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    public static void error(final String key, final Object... params) {
        setLayout();
        log.error(LoggerUtils.getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    private static void setLayout() {
        LoggerUtils.putCalledClassAndMethod(WebApplicationLogger.class);
        LoggerUtils.putWebInfo();
    }



}
