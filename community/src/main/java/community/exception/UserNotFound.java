package community.exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super();
    }

    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
