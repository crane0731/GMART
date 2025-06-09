package gmart.gmart.exception;

/**
 * 사용자 상품 예외
 */
public class ItemCustomException extends RuntimeException {
    public ItemCustomException(String message) {
        super(message);
    }

  public ItemCustomException() {
    super();
  }

  public ItemCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public ItemCustomException(Throwable cause) {
    super(cause);
  }

  protected ItemCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
