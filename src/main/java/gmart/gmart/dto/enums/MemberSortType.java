package gmart.gmart.dto.enums;

/**
 * 회원 정렬 기준
 * suspension_count,reported_count,manner_point,total_spent
 * 계정정지 횟수, 신고당한 횟수, 매너 포인트, 총 결제 금액
 */
public enum MemberSortType {

    SUSPENSION,
    REPORTED,
    MANNERPOINT,
    TOTALSPENT
}
