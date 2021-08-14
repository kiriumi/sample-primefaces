package log;

import javax.enterprise.context.Dependent;

@Dependent
public class WebAccessLogger extends BaseWebLogger {

    @Override
    protected String getBundleName() {
        return "LogMessages";
    }

}
