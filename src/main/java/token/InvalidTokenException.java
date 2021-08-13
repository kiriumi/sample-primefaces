package token;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(final String message) {
        super(message);
    }

    public InvalidTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(final Throwable cause) {
        super(cause);
    }

}
