package gmart.gmart.domain.enums;

/**
 * 에스크로 상태
 */
public enum EscrowStatus {

    NONE,     // 아직 GMoney 차감하지 않은 상태 (구매 요청됨 → 승인 전 or 승인 후 결제 대기중)
    HOLDING,  // GMoney 차감 완료, 시스템이 보관중 (배송 및 구매확정 대기 상태)
    RELEASED, // 구매확정 완료 → 판매자에게 GMoney 지급 완료 상태
    CANCELED  // 거래 취소 처리 완료 → GMoney 환불 완료 상태

}
