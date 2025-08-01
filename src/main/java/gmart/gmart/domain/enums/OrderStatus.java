package gmart.gmart.domain.enums;

/**
 * 주문 상태
 */
public enum OrderStatus {

    REQUESTED, //구매 요청 (구매자)
    CANCELED, //구매 요청 취소 (구매자)
    REJECT, //구매 요청 거절 (판매자)
    ACCEPT, //구매 요청 확인(판매자)
    CONFIRM //구매  확인 (구매자)

}
