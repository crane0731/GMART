package gmart.gmart.domain.enums;

/**
 * 주문 상태
 */
public enum OrderStatus {

    RESERVED,   // 주문예약 -> 주문이 등록
    CANCEL_REQUESTED,//주문 취소 요청 ->구매자가 구매 취소 요청
    CANCELLED,// 주문 취소 -> 판매자가 주문 취소 요청을 승인함
    CONFIRMED,  // 주문확인 -> 판매자가 주문 확인 완료
    REJECTED,    // 주문거절 -> 판매자가 주문 거절
    SHIPPED,// 상품 발송 -> 판매자가 상품을 발송함
    COMPLETED, //구매 확정 -> 구매자가 상품을 받은 후 구매 확정 완료
    REFUND_REQUESTED, //환불 요청 -> 구매자가 환불을 요청
    REFUND_APPROVED, //환불 승인 -> 판매자가 환불 요청을 승인
    REFUND_SHIPPED, //환불 상품 배송 -> 구매자가 다시 상품을 판매자에게 배송함
    REFUNDED //환불 완료 -> 판매자가 상품을 다시 받고 환불완료를 누름

}
