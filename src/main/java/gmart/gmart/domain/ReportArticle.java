package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 신고
 */
@Entity
@Table(name = "report_article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportArticle extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @org.hibernate.annotations.Comment("신고 사유")
    @Column(name = "reason", nullable = false)
    private String reason;

    @org.hibernate.annotations.Comment("신고 처리 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ReportStatus status;


    /**
     * [생성 메서드]
     * @param member 회원
     * @param article 게시글
     * @param reason 신고 이유
     * @return ReportArticle 엔티티
     */
    public static ReportArticle createEntity(Member member, Article article, String reason) {
        ReportArticle reportArticle = new ReportArticle();

        reportArticle.setMember(member);
        reportArticle.setArticle(article);

        reportArticle.reason = reason;
        reportArticle.status = ReportStatus.WAITING;

        return reportArticle;
    }


    /**
     * [비즈니스 로직]
     * 신고 접수
     */
    public void acceptReport() {
        this.status = ReportStatus.ACCEPTED;
    }

    /**
     * [비즈니스 로직]
     * 신고 취소
     */
    public void rejectReport() {
        this.status = ReportStatus.REJECTED;
        this.member.minusReportedCount();
    }


    /**
     * [연관관계 편의 메서드]
     * ReportArticle - Member
     * @param member 회원
     */
    private void setMember(Member member) {
        this.member = member;
        member.addReportArticle(this);
    }

    /**
     * [연관관계 편의 메서드]
     * ReportArticle - Article
     * @param article 게시글
     */
    private void setArticle(Article article) {
        this.article = article;
        article.addReportArticle(this);
    }




}
