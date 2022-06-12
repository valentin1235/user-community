package community.exception;

public class InvalidAccountType extends RuntimeException {

    public InvalidAccountType() {
        super();
    }

    public InvalidAccountType(String message) {
        super(message);
    }

    public InvalidAccountType(String message, Throwable cause) {
        super(message, cause);
    }
}
