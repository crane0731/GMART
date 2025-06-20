package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.ReviewType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리뷰 테이블
 */
@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseAuditingEntity {

    @org.hibernate.annotations.Comment("리뷰 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @org.hibernate.annotations.Comment("리뷰어 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    @org.hibernate.annotations.Comment("리뷰 대상자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private Member reviewee;

    @org.hibernate.annotations.Comment("리뷰 대상 주문 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @org.hibernate.annotations.Comment("리뷰 내용")
    @Column(name = "content")
    private String content;

    @org.hibernate.annotations.Comment("리뷰 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "review_type")
    private ReviewType reviewType;

    @org.hibernate.annotations.Comment("리뷰 점수")
    private Long rating;


    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;
}
