package gmart.gmart.controller.order;

import gmart.gmart.domain.enums.OrderStatus;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.order.SellerOrderListResponseDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 조회 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/order")
public class OrderSearchController {

    private final OrderService orderService; //주문 서비스

    /**
     * [컨트롤러]
     * 판매자의 주문 관리를 위해 주문 리스트를 조회 하는 api
     * @param orderStatus 주문 상태
     * @param page 주문 번호
     * @return  PagedResponseDto<SellerOrderListResponseDto> 페이지 응답 DTO
     */
    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<?>> findSellerOrderInformation(@RequestParam(value = "orderStatus",required = false) OrderStatus orderStatus,
                                                                     @RequestParam(value = "page",defaultValue = "0")int page
                                                                     ) {
        PagedResponseDto<SellerOrderListResponseDto> responseDto = orderService.findSellerOrderInformation(orderStatus, page);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));

    }


}
