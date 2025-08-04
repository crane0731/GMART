package gmart.gmart.dto.order;


import gmart.gmart.domain.Order;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 주문 리스트 응답 DTO
 */
@Getter
@Setter
public class AdminOrderListResponseDto {

    private Long orderId; //주문 아이디

    private Long itemId; //상품 아이디

    private Long buyerId; //구매자 아이디
    private String buyerNickname; //구매자 닉네임
    private String buyerLoginId; //구매자 로그인 아이디

    private Long sellerId; //판매자 아아디
    private String sellerNickname; //판매자 닉네임
    private String sellerLoginId; //판매자 로그인 아이디

    private String orderStatus; //주문 상태
    private Long totalPrice; //총 가격
    private Long paidPrice; //지불 가격
    private Long itemPrice; //상품 가격
    private Long deliveryPrice; //배송비
    private Long usedPoint; //사용포인트
    private String escrowStatus; //에스크로 상태


    /**
     * [생성 메서드]
     * @param order 주문
     * @return AdminOrderListResponseDto
     */
    public static AdminOrderListResponseDto create(Order order) {
        AdminOrderListResponseDto dto = new AdminOrderListResponseDto();
        dto.setOrderId(order.getId());

        dto.setItemId(order.getItem().getId());

        dto.setBuyerId(order.getBuyer().getId());
        dto.setBuyerNickname(order.getBuyer().getNickname());
        dto.setBuyerLoginId(order.getBuyer().getLoginId());

        dto.setSellerId(order.getSeller().getId());
        dto.setSellerNickname(order.getSeller().getNickname());
        dto.setSellerLoginId(order.getSeller().getLoginId());

        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setPaidPrice(order.getPaidPrice());
        dto.setItemPrice(order.getItemPrice());
        dto.setDeliveryPrice(order.getDeliveryPrice());
        dto.setUsedPoint(order.getUsedPoint());
        dto.setEscrowStatus(order.getEscrowStatus().name());

        return dto;

    }


}
