package gmart.gmart.controller.order;

import gmart.gmart.dto.RefundOrderRequestDto;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.delivery.trackingNumberRequestDto;
import gmart.gmart.dto.order.CancelOrderRequestDto;
import gmart.gmart.dto.order.CreateOrderRequestDto;
import gmart.gmart.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 주문 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/order")
public class OrderController {

    private final OrderService orderService; //주문 서비스


    /**
     * [컨트롤러]
     * 구매자 -> 구매버튼
     * 주문 신청(등록)
     * @param itemId 상품 아이디
     * @param requestDto 주문 신청 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/item/{id}")
    public ResponseEntity<ApiResponse<?>> creatOrder(@PathVariable("id") Long itemId, @Valid @RequestBody CreateOrderRequestDto requestDto, BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        orderService.createOrder(itemId, requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 신청 완료")));


    }

    /**
     * [컨트롤러]
     * 구매자가 판매자에게 주문 신청 취소를 요청함
     * 주문 상태가 아직 주문확인 상태 즉,판매자가 주문 확인을 하고 아직 상품을 배송하기 전인 상태 만 가능
     * @param orderId 주문 아이디
     * @param requestDto 취소 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/buyer-cancel-request")
    public ResponseEntity<ApiResponse<?>> revokeOrder(@PathVariable("id")Long orderId,@Valid @RequestBody CancelOrderRequestDto requestDto, BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        orderService.cancelRequestByBuyer(orderId,requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 취소 요청 완료")));
    }


    /**
     * [컨트롤러]
     * 구매자가 주문 취소 처리
     * 아직 판매자가 주문을 확인하기 전 상태에서만 가능 (주문 예약 상태)
     * @param orderId 주문 아이디
     * @param requestDto 취소 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/buyer-cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrderByBuyer(@PathVariable("id")Long orderId, @Valid @RequestBody CancelOrderRequestDto requestDto,BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        orderService.cancelOrderByBuyer(orderId, requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 취소 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 주문확인 처리
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<?>> confirmOrder(@PathVariable("id")Long orderId){
        orderService.confirmOrder(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 확인 완료")));
    }


    /**
     * [컨트롤러]
     * 주문 취소 처리 -> 판매자가 구매자의 구매 요청 주문 거절
     * @param orderId 주문 아이디
     * @param requestDto 취소 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/seller-rejection")
    public ResponseEntity<ApiResponse<?>> rejectOrderRequestBySeller(@PathVariable("id")Long orderId,@Valid @RequestBody CancelOrderRequestDto requestDto, BindingResult bindingResult){

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }
        orderService.rejectOrderRequestBySeller(orderId,requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 거절 완료")));
    }

    /**
     * [컨트롤러]
     * 주문 취소 처리 -> 판매자가 주문 취소
     * @param orderId 주문 아이디
     * @param requestDto 취소 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/seller-cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrderBySeller(@PathVariable("id")Long orderId,@Valid @RequestBody CancelOrderRequestDto requestDto, BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        orderService.cancelOrder(orderId,requestDto);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 취소 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 구매자의 주문 취소 요청을 승인 -> 주문 취소 처리
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/buyer-cancel/seller-accept")
    public ResponseEntity<ApiResponse<?>> acceptCancelOrderBySeller(@PathVariable("id")Long orderId){
        orderService.acceptCancelOrderRequestBySeller(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 취소 승인 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 구매자의 주문 취소 요청을 거절 -> 주문 절차가 원래대로 진행됨
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/buyer-cancel/seller-rejection")
    public ResponseEntity<ApiResponse<?>> rejectCancelOrderBySeller(@PathVariable("id")Long orderId){
        orderService.rejectCancelOrderRequestBySeller(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","주문 취소 거절 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 상품 배송 준비를 완료
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/delivery-preparations")
    public ResponseEntity<ApiResponse<?>> readyDelivery(@PathVariable("id")Long orderId){
        orderService.readyDelivery(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","배송 준비 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 상품 배송 준비 완료를 취소
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/delivery-preparations/cancel")
    public ResponseEntity<ApiResponse<?>> cancelReadyDelivery(@PathVariable("id")Long orderId){
        orderService.cancelReadyDelivery(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","배송 준비 취소 완료")));
    }


    /**
     * [컨트롤러]
     * 판매자가 상품을 배송
     * @param orderId 주문 아이디
     * @param requestDto 배송 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/delivery")
    public ResponseEntity<ApiResponse<?>> shipItem(@PathVariable("id")Long orderId, @Valid @RequestBody trackingNumberRequestDto requestDto, BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        orderService.shipItem(orderId,requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","배송 시작 완료")));
    }

    /**
     * [컨트롤러]
     * 구매자 구매확정
     * 판매자에게 판매 대금 입금
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<?>> completeOrder(@PathVariable("id")Long orderId){
        orderService.completeOrder(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","구매 확정 완료")));
    }

    /**
     * [컨트롤러]
     * 구매자가 환불 요청
     * @param orderId 주문 아이디
     * @param requestDto 환불 요청 DTO
     * @param bindingResult 에러메시지를 바인딩할 객체
     * @return 성공 메시지
     */
    @PostMapping("/{id}/refund-request")
    public ResponseEntity<ApiResponse<?>> refundRequest(@PathVariable("id")Long orderId, @Valid @RequestBody RefundOrderRequestDto requestDto, BindingResult bindingResult) {

        Map<String, String> errorMessages = new HashMap<>();
        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        orderService.refundRequest(orderId,requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","환불 요청 완료")));

    }

    /**
     * [컨트롤러]
     * 판매자가 구매자의 환불요청을 거절함 -> 구매확정
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/refund-request/rejection")
    public ResponseEntity<ApiResponse<?>> rejectRefundRequest(@PathVariable("id")Long orderId){
        orderService.rejectRefundRequest(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","환불 거절 완료 , 구매 확정 완료")));
    }



    //==필드에러가 있는지 확인하는 로직==//
    private boolean errorCheck(BindingResult bindingResult, Map<String, String> errorMessages) {
        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );

            return true;
        }
        return false;
    }
}
