package zaritalk.community.exceptions;

public class NotAuthorized extends RuntimeException{

    public NotAuthorized() {
        super();
    }

    public NotAuthorized(String message) {
        super(message);
    }

    public NotAuthorized(String message, Throwable cause) {
        super(message, cause);
    }
}
