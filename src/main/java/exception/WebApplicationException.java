package exception;

public class WebApplicationException extends RuntimeException {

    public WebApplicationException() {
        super();
    }

    public WebApplicationException(final String message) {
        super(message);
    }

    public WebApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WebApplicationException(final Throwable cause) {
        super(cause);
    }

}
