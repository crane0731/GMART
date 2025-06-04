package gmart.gmart.exception;

/**
 * 건담 정보 관련 예외
 */
public class GundamCustomException extends RuntimeException {

    public GundamCustomException(String message) {
        super(message);
    }

    public GundamCustomException() {
        super();
    }

    public GundamCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public GundamCustomException(Throwable cause) {
        super(cause);
    }
}
