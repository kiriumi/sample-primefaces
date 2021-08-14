package log;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WebAccessLogger  {

    public static void access(final String key, final Object... params) {
        setLayout();
        log.info(LoggerUtils.getLogMessage(key), params);
        ThreadContext.clearAll();
    }

    private static void setLayout() {
        LoggerUtils.putCalledClassAndMethod(WebAccessLogger.class);
        LoggerUtils.putWebInfo();
    }

}
