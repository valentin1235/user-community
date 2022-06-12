package community.exception;

public class PostingNotFound extends RuntimeException {

    public PostingNotFound() {
        super();
    }

    public PostingNotFound(String message) {
        super(message);
    }

    public PostingNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
