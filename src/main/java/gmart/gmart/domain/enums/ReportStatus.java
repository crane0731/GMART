package gmart.gmart.domain.enums;

/**
 * 신고 처리 상태
 */

public enum ReportStatus {

    WAITING,     // 접수됨 (처리 전)
    ACCEPTED,    // 신고 수용 → 게시글 제재 등 조치함
    REJECTED,    // 신고 기각 → 문제가 없는 것으로 판단
}
