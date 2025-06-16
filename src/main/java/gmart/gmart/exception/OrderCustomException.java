package gmart.gmart.exception;

public class OrderCustomException extends RuntimeException {
    public OrderCustomException(String message) {
        super(message);
    }

    public OrderCustomException() {
        super();
    }

    public OrderCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderCustomException(Throwable cause) {
        super(cause);
    }

    protected OrderCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
