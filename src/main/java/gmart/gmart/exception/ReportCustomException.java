package gmart.gmart.exception;

public class ReportCustomException extends RuntimeException {
    public ReportCustomException(String message) {
        super(message);
    }

  public ReportCustomException() {
    super();
  }

  public ReportCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReportCustomException(Throwable cause) {
    super(cause);
  }

  protected ReportCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
