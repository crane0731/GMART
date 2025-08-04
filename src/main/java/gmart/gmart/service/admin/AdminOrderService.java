package gmart.gmart.service.admin;

import gmart.gmart.domain.Order;

import gmart.gmart.dto.order.AdminOrderListResponseDto;
import gmart.gmart.dto.order.AdminOrderResponseDto;

import gmart.gmart.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 주문 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderService{

    private final OrderService orderService; //주문 서비스


    /**
     * [서비스 로직]
     * 거래 완료된 주문 목록 조회
     * @param page 페이지 번호
     * @return AdminOrderResponseDto
     */
    public AdminOrderResponseDto findOrders(int page){

        Page<Order> pageList = orderService.findAllByOrderStatus(page);
        List<AdminOrderListResponseDto> adminOrderListResponseDtos = pageList.getContent().stream().map(AdminOrderListResponseDto::create).toList();

        Long totalTradeCount = orderService.countItemsByOrderStatus();
        Long totalGMoneyAmount = orderService.findTotalGMoneyByOrderStatus();
        Long totalPaidGMoneyAmount = orderService.findTotalPaidGMoneyByOrderStatus();
        Long totalUsedGPointAmount = orderService.findTotalUsedGPointByOrderStatus();



        return createAdminOrderResponseDto(totalTradeCount, totalGMoneyAmount, totalPaidGMoneyAmount, totalUsedGPointAmount, pageList, adminOrderListResponseDtos);

    }

    //==관리자 응답 DTO 생성 메서드==//
    private AdminOrderResponseDto createAdminOrderResponseDto(Long totalTradeCount, Long totalGMoneyAmount, Long totalPaidGMoneyAmount, Long totalUsedGPointAmount, Page<Order> pageList, List<AdminOrderListResponseDto> adminOrderListResponseDtos) {
        return AdminOrderResponseDto.create(totalTradeCount, totalGMoneyAmount, totalPaidGMoneyAmount, totalUsedGPointAmount,
                pageList.getNumber(),
                pageList.getSize(),
                pageList.getTotalPages(),
                pageList.getTotalElements(),
                pageList.isFirst(),
                pageList.isLast(),
                adminOrderListResponseDtos);
    }


}
