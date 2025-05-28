package gmart.gmart.exception;

public class InquiryCustomException extends RuntimeException {


    public InquiryCustomException(String message) {
        super(message);
    }

    public InquiryCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public InquiryCustomException(Throwable cause) {
        super(cause);
    }
}
