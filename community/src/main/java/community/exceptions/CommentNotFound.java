package community.exceptions;

public class CommentNotFound extends RuntimeException {
    public CommentNotFound() {
        super();
    }

    public CommentNotFound(String message) {
        super(message);
    }

    public CommentNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
