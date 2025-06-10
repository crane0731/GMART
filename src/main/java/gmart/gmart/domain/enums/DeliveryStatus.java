package gmart.gmart.domain.enums;

/**
 * 배송 상태
 */
public enum DeliveryStatus {

    READY,      // 배송 준비중 (주문 직후 기본 상태)
    SHIPPING,   // 배송중 (판매자가 '발송 처리' 버튼 눌러서 송장 입력 시)
    DELIVERED   // 배송완료 (구매자가 '구매 확정 눌렀을 때)

}
