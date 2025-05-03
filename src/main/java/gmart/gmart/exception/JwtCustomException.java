package gmart.gmart.exception;

/**
 * JWT 토큰 관련 커스텀 예외
 */
public class JwtCustomException extends RuntimeException {

    public JwtCustomException(String message) {
        super(message);
    }

  public JwtCustomException() {
    super();
  }

  public JwtCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public JwtCustomException(Throwable cause) {
    super(cause);
  }
}
