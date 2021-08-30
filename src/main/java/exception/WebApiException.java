package exception;

public class WebApiException extends RuntimeException {

    public WebApiException() {
        super();
    }

    public WebApiException(final String message) {
        super(message);
    }

    public WebApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WebApiException(final Throwable cause) {
        super(cause);
    }

}
