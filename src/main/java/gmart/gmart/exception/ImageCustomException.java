package gmart.gmart.exception;

/**
 * 이미지 관련 사용자 예외
 */
public class ImageCustomException extends RuntimeException {

  public ImageCustomException(String message) {
    super(message);
  }

  public ImageCustomException() {
    super();
  }

  public ImageCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageCustomException(Throwable cause) {
    super(cause);
  }
}
