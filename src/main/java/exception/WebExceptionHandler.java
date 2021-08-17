package exception;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.lang3.StringUtils;

import log.WebApplicationLogger;

public class WebExceptionHandler extends ExceptionHandlerWrapper {

    public static final String FLASH_KEY_ERROR_MESSAGE = "exception.WebExceptionHandler.errorMessage";
    public static final String FLASH_KEY_EXCEPTION = "exception.WebExceptionHandler.exception";

    private final ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
    public WebExceptionHandler(final ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() {

        for (Iterator<ExceptionQueuedEvent> queuedEvent = getUnhandledExceptionQueuedEvents().iterator(); queuedEvent
                .hasNext();) {

            // 発生した例外情報の取得
            ExceptionQueuedEventContext eventContext = queuedEvent.next().getContext();
            Throwable throwable = getRootCause(eventContext.getException()).getCause();
            if (throwable == null) {
                throwable = eventContext.getException();
            }

            // メッセージを設定
            FacesContext facesContext = eventContext.getContext();
            String errorMessage = "予期せぬエラーが発生しました";
            if (throwable instanceof WebApplicationException) {
                errorMessage = StringUtils.defaultString(throwable.getMessage(), errorMessage);
            }

            // エラー情報の引継ぎ
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.put(FLASH_KEY_ERROR_MESSAGE, errorMessage);
            flash.keep(FLASH_KEY_ERROR_MESSAGE);
            flash.put(FLASH_KEY_EXCEPTION, throwable);
            flash.keep(FLASH_KEY_EXCEPTION);

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

    public static String getMessage() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(FLASH_KEY_ERROR_MESSAGE);
    }

    public static Throwable getException() {
        return (Throwable) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(FLASH_KEY_EXCEPTION);
    }
}
