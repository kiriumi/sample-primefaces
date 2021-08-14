package exception;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.lang3.StringUtils;

import log.WebApplicationLogger;

public class WebExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
    public WebExceptionHandler(final ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() {

        for (Iterator<ExceptionQueuedEvent> queuedEvent = getUnhandledExceptionQueuedEvents().iterator(); queuedEvent
                .hasNext();) {

            ExceptionQueuedEventContext eventContext = queuedEvent.next().getContext();
            Throwable throwable = getRootCause(eventContext.getException()).getCause(); // アプリケーション例外を取得
            if (throwable == null) {
                throwable = eventContext.getException();
            }

            // メッセージを設定
            FacesContext facesContext = eventContext.getContext();
            String errorMessage = StringUtils.defaultString(throwable.getMessage(), "予期せぬエラーが発生しました");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
            facesContext.getExternalContext().getFlash().setKeepMessages(true); // リダイレクトしてもFacesMessageが消えないように設定

            WebApplicationLogger.error("error", throwable.getMessage(), throwable);

            try {
                // エラー画面に遷移
                ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
                String contextPath = facesContext.getExternalContext().getRequestContextPath();
                facesContext.getExternalContext().redirect(contextPath + bundle.getString("error.page"));

            } catch (IOException e) {
                WebApplicationLogger.error("error", e.getMessage(), throwable);

            } finally {
                queuedEvent.remove();
            }
        }
        wrapped.handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }
}
