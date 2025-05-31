package gmart.gmart.dto.reportarticle;


import gmart.gmart.domain.ReportArticle;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 단일 상세조회 DTO
 */
@Getter
@Setter
public class ReportArticleDetailResponseDto {

    private Long memberId; //회원 아이디
    private String nickname; //닉네임
    private String loginId; //로그인 아이디(이메일)
    private String mannerGrade; //매너 등급
    private String memberRole; //회원 권한

    private Long articleId; //게시글 아이디
    private String articleTitle;

    private Long reportedMemberId; //신고당한 회원 아이디
    private String reportedNickname; //닉네임
    private String reportedLoginId; //로그인 아이디(이메일)
    private String reportedMannerGrade; //매너 등급
    private String reportedMemberRole; //회원 권한

    private Long reportArticleId;
    private String reason; //신고 사유
    private String reportStatus; //신고 처리 상태

    private String createdAt; //생성일
    private String updatedAt; //수정일

    /**
     * [생성 메서드]
     * @param reportArticle 게시글 신고 엔티티
     * @return DTO
     */
    public static ReportArticleDetailResponseDto createDto(ReportArticle reportArticle) {
        ReportArticleDetailResponseDto dto = new ReportArticleDetailResponseDto();

        dto.memberId = reportArticle.getMember().getId();
        dto.nickname = reportArticle.getMember().getNickname();
        dto.loginId = reportArticle.getMember().getLoginId();
        dto.mannerGrade = reportArticle.getMember().getMannerGrade().toString();
        dto.memberRole = reportArticle.getMember().getMemberRole().toString();

        dto.articleId = reportArticle.getArticle().getId();
        dto.articleTitle = reportArticle.getArticle().getTitle();

        dto.reportedMemberId = reportArticle.getArticle().getMember().getId();
        dto.reportedNickname = reportArticle.getArticle().getMember().getNickname();
        dto.reportedLoginId = reportArticle.getArticle().getMember().getLoginId();
        dto.reportedMannerGrade = reportArticle.getArticle().getMember().getMannerGrade().toString();
        dto.reportedMemberRole= reportArticle.getArticle().getMember().getMemberRole().toString();

        dto.reportArticleId = reportArticle.getId();
        dto.reason=reportArticle.getReason();
        dto.reportStatus=reportArticle.getStatus().toString();

        dto.createdAt= DateFormatUtil.DateFormat(reportArticle.getCreatedDate());
        dto.updatedAt= DateFormatUtil.DateFormat(reportArticle.getUpdatedDate());

        return dto;
    }

}
