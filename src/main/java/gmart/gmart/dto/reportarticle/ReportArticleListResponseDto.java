package gmart.gmart.dto.reportarticle;

import gmart.gmart.domain.ReportArticle;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 신고 리스트 응답 DTO
 */
@Getter
@Setter
public class ReportArticleListResponseDto {

    private Long reportArticleId; //게시글 신고 아이디
    private String reportStatus; //신고 처리 상태

    private Long memberId; //신고자 회원 아이디

    private Long articleId;// 신고당한 게시글 아이디

    private Long reportedId; //신고당한 회원 아이디

    private String createAt; //생성일


    /**
     * [생성 메서드]
     * ReportArticle -> ReportArticleListResponseDto
     * @param reportArticle 게시글 신고 엔티티
     * @return ReportArticleListResponseDto DTO
     */
    public static ReportArticleListResponseDto createDto(ReportArticle reportArticle) {
        ReportArticleListResponseDto dto = new ReportArticleListResponseDto();
        dto.setReportArticleId(reportArticle.getId());
        dto.setReportStatus(reportArticle.getStatus().toString());

        dto.setMemberId(reportArticle.getMember().getId());
        dto.setArticleId(reportArticle.getArticle().getId());
        dto.setReportedId(reportArticle.getArticle().getMember().getId());

        dto.setCreateAt(DateFormatUtil.DateFormat(reportArticle.getCreatedDate()));
        return dto;
    }


}
