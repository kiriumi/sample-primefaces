package exception;

public class AccountLokedException extends WebApplicationException {

    private static final String MESSAGE = "アカウントロック中です";

    public AccountLokedException() {
        super(MESSAGE);
    }

    public AccountLokedException(final Throwable cause) {
        super(MESSAGE, cause);
    }

}
