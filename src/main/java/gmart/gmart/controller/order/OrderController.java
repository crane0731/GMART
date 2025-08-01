package gmart.gmart.controller.order;


import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.order.CreateOrderRequestDto;
import gmart.gmart.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * 구매 신청(등록)
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
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","구매 신청 완료")));
    }

    /**
     * [컨트롤러]
     * 구매자가 구매 신청 취소
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrder(@PathVariable("id")Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","구매 신청 취소 완료")));
    }

    /**
     * [컨트롤러]
     * 판매자가 구매 신청 요청 거절
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<?>> rejectOrder(@PathVariable("id")Long orderId){
        orderService.rejectOrder(orderId);;
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","구매 신청 거절 완료")));
    }


    /**
     * [컨트롤러]
     * 판매자가 구매 신청 확인 -> 주문 확인 처리
     * @param orderId 주문 아이디
     * @return 성공 메시지
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<?>> acceptOrder(@PathVariable("id")Long orderId){
        orderService.acceptOrder(orderId);;
        return ResponseEntity.ok().body(ApiResponse.success(Map.of("message","구매 신청 수락 완료")));
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
