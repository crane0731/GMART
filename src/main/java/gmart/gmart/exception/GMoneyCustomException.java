package gmart.gmart.exception;

/**
 * 건머니 관련 사용자 예외
 */
public class GMoneyCustomException extends RuntimeException {
  public GMoneyCustomException(String message) {
    super(message);
  }

  public GMoneyCustomException() {
    super();
  }

  public GMoneyCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public GMoneyCustomException(Throwable cause) {
    super(cause);
  }
}
