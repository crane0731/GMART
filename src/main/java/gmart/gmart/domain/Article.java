package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ArticleReportedStatus;
import gmart.gmart.exception.ArticleCustomException;
import gmart.gmart.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글
 */
@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Comment("제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("회원 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Comment("내용")
    @Column(name = "content", nullable = false)
    private String content;

    @Comment("좋아요 수")
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Comment("조회 수")
    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Comment("신고 수")
    @Column(name = "reported_count",nullable = false)
    private Long reportedCount;

    @Comment("댓글 수")
    @Column(name = "comment_count",nullable = false)
    private Long commentCount;

    @Enumerated(EnumType.STRING)
    @Comment("게시글 신고 상태")
    @Column(name = "article_reported_status", nullable = false)
    private ArticleReportedStatus articleReportedStatus;

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleImage> articleImages=new ArrayList<>();

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<gmart.gmart.domain.Comment> comments=new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LikeArticle> likeArticles=new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ReportArticle> reportArticles=new ArrayList<>();

    /**
     * [생성 메서드]
     */
    public static Article createEntity(Member member,String title, String content){

        Article article=new Article();

        article.title=title;
        article.content=content;
        article.member = member;

        article.articleReportedStatus=ArticleReportedStatus.NOT_REPORTED;
        article.viewCount=0L;
        article.likeCount=0L;
        article.commentCount=0L;
        article.reportedCount=0L;

        return article;
    }

    /**
     * [비즈니스 로직]
     * 업데이트
     * @param title 게시글
     * @param content 내용
     * @param articleImages 이미지 리스트
     */
    public void update(String title, String content, List<ArticleImage> articleImages){
        this.title=title;
        this.content=content;

        System.out.println("기존 이미지 수: " + this.getArticleImages().size());
        updateImage(articleImages);

    }

    /**
     * [비즈니스 로직]
     * 이미지 업데이트
     * @param newArticleImages 이미지 리스트
     */
    private void updateImage(List<ArticleImage> newArticleImages){

        this.articleImages.clear();
        this.articleImages.addAll(newArticleImages);

        for (ArticleImage newImage : newArticleImages) {
            newImage.setArticle(this);
        }

    }

    /**
     * [비즈니스 로직]
     * 게시글 이미지 생성 + 세팅
     */
    public void attachArticleImage(String imageUrl){

        ArticleImage articleImage = ArticleImage.createEntity(imageUrl);
        addArticleImage(articleImage);
    }
    /**
     * [비즈니스 로직]
     * 게시글 - 게시글 좋아요 양방향 세팅 + 게시글 좋아요 수 증가
     */
    public void like(LikeArticle likeArticle){
        addLikeArticle(likeArticle);
        this.likeCount++;
    }

    /**
     * [비즈니스 로직]
     * 게시글 좋아요 수 감소
     */
    public void unlike(LikeArticle likeArticle){
        deleteLikeArticle(likeArticle);
        this.likeCount--;
    }

    /**
     * [비즈니스 로직]
     * 회원이 게시글의 주인인지 확인 하는 로직
     * @param loginMember 로그인한 회원
     */
    public void validateOwner(Member loginMember) {
        if (!this.member.getId().equals(loginMember.getId())) {
            throw new ArticleCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    /**
     * [비즈니스 로직]
     * 신고수 감소 -1
     */
    protected void minusReportedCount(){
        reportedCount--;
    }

    /**
     * [비즈니스 로직]
     * 조회 수 증가 +1
     */
    public void addViewCount(){
        this.viewCount++;
    }


    /**
     * [연관관계 편의 메서드]
     * 게시글 이미지 양방향 세팅
     */
    public void addArticleImage(ArticleImage articleImage){
        this.articleImages.add(articleImage);
        articleImage.setArticle(this);
    }

    /**
     * [연관관계 편의 메서드]
     * 게시글 이미지 양방향 삭제
     */
    public void deleteArticleImage(ArticleImage articleImage){
        articleImage.setArticle(null);
        this.articleImages.remove(articleImage);
    }

    /**
     * [연관관계 편의 메서드]
     * 게시글 좋아요 양방향 세팅
     */
    private void addLikeArticle(LikeArticle likeArticle){
        this.likeArticles.add(likeArticle);
        likeArticle.setArticle(this);

    }

    /**
     * [연관관계 편의 메서드]
     * 게시글 좋아요 양방향 삭제
     */
    private void deleteLikeArticle(LikeArticle likeArticle){
        this.likeArticles.remove(likeArticle);
        likeArticle.setArticle(null);
    }

    /**
     *[비즈니스 로직]
     *리스트에 값 추가
     *신고 수 증가 +1
     *게시글 신고 상태 -> REPORTED
     * @param reportArticle
     */
    protected void addReportArticle(ReportArticle reportArticle){
        reportArticles.add(reportArticle);
        reportedCount++;
        articleReportedStatus = ArticleReportedStatus.REPORTED;
    }

    /**
     * [비즈니스 로직]
     * 게시글의 신고 상태를 신고 안됨(NOT_REPORTED)로 변경
     */
    public void changeReportedStatusToNotReported(){
        articleReportedStatus = ArticleReportedStatus.NOT_REPORTED;
    }
}
