package gmart.gmart.dto.report;

import gmart.gmart.domain.Report;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 리스트 목록 응답 DTO
 */
@Getter
@Setter
public class ReportListResponseDto {

    private Long reportId; //신고 아이디
    private Long reporterId; //신고자 아이디
    private Long reportedMemberId; //피신고자 아이디
    private Long itemId; //상품 아이디
    private String reporterRole; //신고자 역할
    private String reportStatus;//신고 처리 상태


    /**
     * [생성 메서드]
     * @param report 신고 엔티티
     * @return ReportListResponseDto 응답 DTO
     */
    public static ReportListResponseDto create(Report report) {
        ReportListResponseDto dto = new ReportListResponseDto();
        dto.setReportId(report.getId());
        dto.setReporterId(report.getReporter().getId());
        dto.setReportedMemberId(report.getReportedMember().getId());
        dto.setItemId(report.getItem().getId());
        dto.setReporterRole(report.getReporterRole().toString());
        dto.setReportStatus(report.getReportStatus().toString());
        return dto;
    }

}
