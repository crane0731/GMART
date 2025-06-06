package gmart.gmart.exception;

/**
 * 건포인트 관련 커스텀 예외
 */
public class GPointCustomException extends RuntimeException {
    public GPointCustomException(String message) {
        super(message);
    }

    public GPointCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public GPointCustomException(Throwable cause) {
        super(cause);
    }

    public GPointCustomException() {
    }
}
