package gmart.gmart.dto.article;


import gmart.gmart.domain.Article;
import gmart.gmart.domain.enums.ArticleReportedStatus;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 단일 상세조회 DTO
 */
@Getter
@Setter
public class ArticleDetailResponseDto {

    private Long memberId;
    private String nickname; //닉네임
    private String loginId; //로그인 아이디(이메일)
    private String mannerGrade; //매너 등급
    private String memberRole; //회원 권한

    private Long articleId; //게시글 아이디
    private String title; //제목
    private String content; //내용

    private Long commentCount; //댓글 수
    private Long viewCount; //조회 수
    private Long likeCount; //좋아요 수
    private Long reportedCount; //신고 수

    private ArticleReportedStatus reportedStatus; //게시글 신고 상태

    private String createdAt; //생성일
    private String updatedAt; //수정일


    /**
     * [생성메서드]
     * @param article 게시글 엔티티
     * @return ArticleResponseDto DTO
     */
    public static ArticleDetailResponseDto createDto(Article article) {

        ArticleDetailResponseDto dto = new ArticleDetailResponseDto();
        dto.memberId = article.getMember().getId();
        dto.nickname = article.getMember().getNickname();
        dto.loginId = article.getMember().getLoginId();
        dto.mannerGrade = article.getMember().getMannerGrade().toString();
        dto.memberRole = article.getMember().getMemberRole().toString();

        dto.articleId = article.getId();
        dto.title = article.getTitle();
        dto.content = article.getContent();
        dto.commentCount = article.getCommentCount();
        dto.viewCount = article.getViewCount();
        dto.likeCount = article.getLikeCount();
        dto.reportedCount = article.getReportedCount();
        dto.reportedStatus=ArticleReportedStatus.NOT_REPORTED;

        dto.createdAt=DateFormatUtil.DateFormat(article.getCreatedDate());
        dto.updatedAt = DateFormatUtil.DateFormat(article.getUpdatedDate());

        return dto;

    }

}
