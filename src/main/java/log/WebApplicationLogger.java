package log;

import javax.enterprise.context.Dependent;

@Dependent
public class WebApplicationLogger extends BaseWebLogger {

    @Override
    protected String getBundleName() {
        return "LogMessagesApplication";
    }

}
