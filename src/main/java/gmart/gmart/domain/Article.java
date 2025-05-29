package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.ArticleReportedStatus;
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

    @Enumerated(EnumType.STRING)
    @Comment("게시글 신고 상태")
    @Column(name = "article_reported_status", nullable = false)
    private ArticleReportedStatus articleReportedStatus;

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleImage> articleImages=new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<gmart.gmart.domain.Comment> comments=new ArrayList<>();

}
