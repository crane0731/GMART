package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 좋아요
 */
@Entity
@Table(
        name = "like_article",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_like_article_member_article",
                        columnNames = {"member_id", "article_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;


    /**
     * [setter]
     * Article 게시글
     */
    protected void setArticle(Article article) {
        this.article = article;
    }

    /**
     * [setter]
     * Member 회원
     */
    protected void setMember(Member member) {
        this.member = member;
    }

    /**
     * [생성 메서드]
     * LikeArticle 게시글 좋아요
     */
    public static LikeArticle createEntity(Member member, Article article) {
        LikeArticle likeArticle = new LikeArticle();
        member.addLikeArticle(likeArticle);
        article.like(likeArticle);
        return likeArticle;
    }

}
