package gmart.gmart.exception;

public class AdminMessageCustomException extends RuntimeException {
    public AdminMessageCustomException(String message) {
        super(message);
    }

    public AdminMessageCustomException() {
        super();
    }

    public AdminMessageCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminMessageCustomException(Throwable cause) {
        super(cause);
    }

    protected AdminMessageCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
