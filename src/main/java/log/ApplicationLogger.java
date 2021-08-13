package log;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;

import org.slf4j.MDC;

@Dependent
public class ApplicationLogger extends BaseLogger implements Serializable {

    @Inject
    ExternalContext externalContext;

    @Override
    protected void putCustomInfo() {
        String contextPath = externalContext.getApplicationContextPath();
        MDC.put("contextPath", contextPath.substring(1)); // 頭に/があるため
    }

}
