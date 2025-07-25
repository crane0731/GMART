package gmart.gmart.exception;

import gmart.gmart.dto.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 커스텀 예외 핸들러
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ImageCustomException.class)
    public ResponseEntity<Object> handleImageCustomException(ImageCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(JwtCustomException.class)
    public ResponseEntity<Object> handleJwtCustomException(JwtCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InquiryCustomException.class)
    public ResponseEntity<Object> handleInquiryCustomException(InquiryCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GundamCustomException.class)
    public ResponseEntity<Object> handleGundamCustomException(GundamCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GMoneyCustomException.class)
    public ResponseEntity<Object> handleGMoneyCustomException(GMoneyCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GPointCustomException.class)
    public ResponseEntity<Object> handleGPointCustomException(GPointCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(StoreCustomException.class)
    public ResponseEntity<Object> handleStoreCustomException(StoreCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ItemCustomException.class)
    public ResponseEntity<Object> handleItemCustomException(ItemCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ReportCustomException.class)
    public ResponseEntity<Object> handleReportCustomException(ReportCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(AdminMessageCustomException.class)
    public ResponseEntity<Object> handleAdminMessageCustomException(AdminMessageCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(OrderCustomException.class)
    public ResponseEntity<Object> handleOrderMessageCustomException(OrderCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ReviewCustomException.class)
    public ResponseEntity<Object> handleReviewMessageCustomException(ReviewCustomException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }
}
