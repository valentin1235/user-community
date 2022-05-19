package zaritalk.community.exceptions;

public class AccountTypeNotExist extends RuntimeException{

    public AccountTypeNotExist() {}

    public AccountTypeNotExist(String message) {
        super(message);
    }

    public AccountTypeNotExist(String message, Throwable cause) {
        super(message, cause);
    }
}
