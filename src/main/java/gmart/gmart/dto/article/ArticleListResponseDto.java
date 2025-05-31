package gmart.gmart.dto.article;

import gmart.gmart.domain.Article;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 리스트 응답 DTO
 */
@Getter
@Setter
public class ArticleListResponseDto {

    private Long memberId; //게시글 작성자 아이디
    private String nickname; //게시글 작성자 닉네임

    private Long articleId;// 게시글 아이디
    private String title; // 게시글 제목

    private Long viewCount; //조회 수
    private Long likeCount; // 좋아요 수
    private Long commentCount;// 댓글 수

    private String createdAt; // 생성일


    /**
     * [생성 메서드]
     * Article -> ArticleListResponseDto
     * @param article 게시글 엔티티
     * @return ArticleListResponseDto DTO
     */
    public static ArticleListResponseDto createDto(Article article) {
        ArticleListResponseDto dto = new ArticleListResponseDto();
        dto.memberId = article.getMember().getId();
        dto.nickname = article.getMember().getNickname();

        dto.articleId = article.getId();
        dto.title = article.getTitle();

        dto.viewCount = article.getViewCount();
        dto.likeCount = article.getLikeCount();
        dto.commentCount = article.getCommentCount();

        dto.createdAt= DateFormatUtil.DateFormat(article.getCreatedDate());
        return dto;
    }
}
