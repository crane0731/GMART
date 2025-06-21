package gmart.gmart.exception;

public class ReviewCustomException extends RuntimeException {
    public ReviewCustomException(String message) {
        super(message);
    }

    public ReviewCustomException() {
        super();
    }

    public ReviewCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewCustomException(Throwable cause) {
        super(cause);
    }

    protected ReviewCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
