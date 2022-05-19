package zaritalk.community.exceptions;

public class AccountTypeMismatch extends RuntimeException{
    public AccountTypeMismatch() {
        super();
    }

    public AccountTypeMismatch(String message) {
        super(message);
    }

    public AccountTypeMismatch(String message, Throwable cause) {
        super(message, cause);
    }
}
