package log;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION - 10)
@WebAccessLogging
public class WebAccessLoggingInterceptor implements Serializable {

    @Inject
   private FacesContext facesContext;

    @AroundInvoke
    public Object around(final InvocationContext context) throws Exception {

        PhaseId currentPhaseId = facesContext.getCurrentPhaseId();

        if (!currentPhaseId.equals(PhaseId.INVOKE_APPLICATION)) {
            // アクション以外は何もしない
            // ※immediateのアクションは、Apply Request Valueフェーズのため注意
            return context.proceed();
        }

        // そのままだとProxyである印が付くため除去
        String classNameWithoutProxy = context.getTarget().getClass().getName().replaceAll("\\$Proxy.*$", "");
        String methodName = context.getMethod().getName();

        WebAccessLogger.access("access", classNameWithoutProxy, methodName, "開始");
        Object result = context.proceed();
        WebAccessLogger.access("access", classNameWithoutProxy, methodName, "終了");

        return result;
    }

}
