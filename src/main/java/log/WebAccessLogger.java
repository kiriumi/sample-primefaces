package log;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import util.MessageUtils;

@Log4j2
public class WebAccessLogger {

    public static void access(final String id, final Object... params) {
        setLayout();
        log.info(MessageUtils.getMessage(id, params));
        ThreadContext.clearAll();
    }

    private static void setLayout() {
        LoggerUtils.putCalledClassAndMethod(WebAccessLogger.class);
        LoggerUtils.putWebInfo();
    }

}
