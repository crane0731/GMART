package gmart.gmart.exception;

import gmart.gmart.dto.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 커스텀 예외 핸들러
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ImageCustomException.class)
    public ResponseEntity<Object> handleImageCustomException(ImageCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(JwtCustomException.class)
    public ResponseEntity<Object> handleJwtCustomException(JwtCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InquiryCustomException.class)
    public ResponseEntity<Object> handleInquiryCustomException(InquiryCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GundamCustomException.class)
    public ResponseEntity<Object> handleGundamCustomException(GundamCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GMoneyCustomException.class)
    public ResponseEntity<Object> handleGMoneyCustomException(GMoneyCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GPointCustomException.class)
    public ResponseEntity<Object> handleGPointCustomException(GPointCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(StoreCustomException.class)
    public ResponseEntity<Object> handleStoreCustomException(StoreCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ItemCustomException.class)
    public ResponseEntity<Object> handleItemCustomException(ItemCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ReportCustomException.class)
    public ResponseEntity<Object> handleReportCustomException(ReportCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(AdminMessageCustomException.class)
    public ResponseEntity<Object> handleAdminMessageCustomException(AdminMessageCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(OrderCustomException.class)
    public ResponseEntity<Object> handleOrderMessageCustomException(OrderCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ReviewCustomException.class)
    public ResponseEntity<Object> handleReviewMessageCustomException(ReviewCustomException ex) {
        log.error("CustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }
}
