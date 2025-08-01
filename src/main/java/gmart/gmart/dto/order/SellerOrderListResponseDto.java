package gmart.gmart.dto.order;

import gmart.gmart.domain.Order;
import gmart.gmart.dto.AddressDto;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 판매자 주문 리스트 응답 DTO
 */
@Getter
@Setter
public class SellerOrderListResponseDto {

    private Long orderId; //주문 아이디

    private Long sellerId; //판매자 아이디
    private String sellerNickname; //판매자 닉네임

    private Long buyerId; //구매자 아이디
    private String buyerNickname; //구매자 닉네임
    private AddressDto buyerAddress; //구매자 주소
    private String buyerPhone; //구매자 전화번호


    private Long itemId; //상품 아이디
    private String itemName; //상품 이름

    private Long itemPrice; //상품 가격
    private Long deliveryPrice; //배송비
    private Long totalPrice; //총 가격
    private Long paidPrice; //결제 가격
    private Long usedGPoint; //사용 건포인트

    private String orderStatus;//주문 상태

    private String createdAt; //생성일
    private String updatedAt; //수정일


    /**
     * [생성 메서드]
     * @param order 주문 엔티티
     * @return SellerOrderListResponseDto 응답 DTO
     */
    public static SellerOrderListResponseDto create(Order order){

        SellerOrderListResponseDto dto = new SellerOrderListResponseDto();

        dto.setOrderId(order.getId());

        dto.setSellerId(order.getSeller().getId());
        dto.setSellerNickname(order.getSeller().getNickname());

        dto.setBuyerId(order.getBuyer().getId());
        dto.setBuyerNickname(order.getBuyer().getNickname());
        dto.setBuyerAddress(AddressDto.createDto(order.getBuyer().getAddress()));
        dto.setBuyerPhone(order.getBuyer().getPhoneNumber());


        dto.setItemId(order.getItem().getId());
        dto.setItemName(order.getItem().getTitle());
        dto.setItemPrice(order.getItemPrice());
        dto.setDeliveryPrice(order.getDeliveryPrice());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setPaidPrice(order.getPaidPrice());
        dto.setUsedGPoint(order.getUsedPoint());

        dto.setOrderStatus(order.getOrderStatus().toString());

        dto.setCreatedAt(DateFormatUtil.DateFormat(order.getCreatedDate()));
        dto.setUpdatedAt(DateFormatUtil.DateFormat(order.getUpdatedDate()));
        return dto;

    }
}
