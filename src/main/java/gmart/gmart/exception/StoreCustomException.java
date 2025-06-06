package gmart.gmart.exception;

/**
 * 상점 관련 커스텀 에러
 */
public class StoreCustomException extends RuntimeException {
    public StoreCustomException(String message) {
        super(message);
    }

    public StoreCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreCustomException(Throwable cause) {
        super(cause);
    }

    public StoreCustomException() {
    }
}
