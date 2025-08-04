package gmart.gmart.controller.admin;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 주문 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart/admin/order")
public class AdminOrderController {

    private final AdminOrderService adminOrderService; //관리자 주문 서비스

    /**
     * [컨트롤러]
     * 관리자 - 거래 완료된 주문 목록 조회
     * @param page 페이지 번호
     * @return AdminOrderResponseDto
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> findConfirmedOrders(@RequestParam(value = "page",defaultValue = "0") int page){

        return ResponseEntity.ok(ApiResponse.success(adminOrderService.findOrders(page)));

    }


}
