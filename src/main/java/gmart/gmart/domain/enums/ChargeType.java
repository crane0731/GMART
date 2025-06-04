package gmart.gmart.domain.enums;

/**
 * 건머니 충전 방식
 *
 *     PAYMENT : 유저가 직접 결제해서 충전하는 경우
 *     EVENT : 이벤트를 통해 충전되는 경우
 *     ADMIN : 관리자가 직접 충전해주는 경우 (예외 사항)
 *     REFUND : 환불
 */
public enum ChargeType {

    PAYMENT,
    EVENT,
    ADMIN,
    REFUND
}
