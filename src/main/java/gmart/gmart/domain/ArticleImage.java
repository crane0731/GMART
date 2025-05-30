package gmart.gmart.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 게시글 이미지
 */
@Entity
@Table(name = "article_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_image_id")
    private Long id;

    @Comment("이미지 URL")
    @Column(name = "image_url",nullable = false)
    private String imageUrl;


    @Comment("게시글 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    /**
     * [생성 메서드]
     */
    public static ArticleImage createEntity(String imageUrl){
        ArticleImage articleImage = new ArticleImage();
        articleImage.imageUrl = imageUrl;
        return articleImage;
    }

    /**
     * [setter]
     * Article 게시글 세팅
     */
    protected void setArticle(Article article){
        this.article = article;
    }

}
