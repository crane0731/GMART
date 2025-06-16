package gmart.gmart.domain.enums;

/**
 * 건포인트 변화 타입
 *
 *     PURCHASE :구매
 *     SALE :판매
 *     REFUND :환불
 *     CANCEL: 취소
 *     EVENT :이벤트
 *     ADMIN :관리자
 */
public enum GPointDeltaType {

    PURCHASE,       // 구매
    SALE,           // 판매
    REFUND,         // 환불
    CANCEL,         //취소
    EVENT,          //이벤트
    ADMIN           //관리자
}
