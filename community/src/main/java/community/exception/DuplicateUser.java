package community.exception;

public class DuplicateUser extends RuntimeException{
    public DuplicateUser() {
        super();
    }

    public DuplicateUser(String message) {
        super(message);
    }

    public DuplicateUser(String message, Throwable cause) {
        super(message, cause);
    }
}
