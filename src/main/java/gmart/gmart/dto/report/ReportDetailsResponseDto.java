package gmart.gmart.dto.report;

import gmart.gmart.domain.Report;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 상제 조회 응답 DTO
 */
@Getter
@Setter
public class ReportDetailsResponseDto {


    public Long reportId; //신고 아이디
    public String reason; //신고 사유
    public String reportStatus; //신고 처리 상태

    public Long reporterId; //신고자 아이디
    public String reporterNickname; //신고자 닉네임
    public String reporterLoginId; //신고자 로그인 아이디
    public String reporterRole; //신고자 역할

    public Long reportedMemberId; //피신고자 아이디
    public String reportedMemberNickname; //피신고자 닉네임
    public String reportedMemberLoginId; //피신고자 로그인 아이디

    public Long itemId;//상품 아이디


    /**
     * [생성 메서드]
     * @param report 신고 엔티티
     * @return ReportDetailsResponseDto 응답 DTO
     */
    public static ReportDetailsResponseDto create(Report report) {
        ReportDetailsResponseDto dto = new ReportDetailsResponseDto();

        dto.setReportId(report.getId());
        dto.setReason(report.getReason());
        dto.setReportStatus(report.getReportStatus().toString());

        dto.setReporterId(report.getReporter().getId());
        dto.setReporterNickname(report.getReporter().getNickname());
        dto.setReporterLoginId(report.getReporter().getLoginId());
        dto.setReporterRole(report.getReporterRole().toString());

        dto.setReportedMemberId(report.getReportedMember().getId());
        dto.setReportedMemberNickname(report.getReportedMember().getNickname());
        dto.setReportedMemberLoginId(report.getReportedMember().getLoginId());

        dto.setItemId(report.getItem().getId());
        return dto;
    }

}
