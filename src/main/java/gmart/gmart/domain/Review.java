package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.ReviewType;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.ReviewCustomException;
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
    @Column(name = "rating")
    private Long rating;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    /**
     * [생성 메서드]
     * @param reviewer 리뷰어
     * @param reviewee 리뷰
     * @param order 주문
     * @param content 내용
     * @param reviewType 리뷰 타입
     * @param rating 점수
     * @return Review 리뷰 엔티티
     */
    public static Review create(Member reviewer, Member reviewee, Order order, String content, ReviewType reviewType, Long rating) {
        Review review = new Review();

        review.setReviewer(reviewer);
        review.setReviewee(reviewee);

        review.order = order;
        order.reviewedOrder();

        review.content = content;
        review.reviewType = reviewType;
        review.rating = rating;
        review.deleteStatus = DeleteStatus.UNDELETED;
        return review;

    }

    /**
     * [연관관계 편의메서드]
     * @param reviewer
     */
    public void setReviewer(Member reviewer) {
        this.reviewer = reviewer;
        reviewer.getWrittenReviews().add(this);
    }

    /**
     * [연관관계 편의 메서드]
     * @param reviewee
     */
    public void setReviewee(Member reviewee) {
        this.reviewee = reviewee;
        reviewee.getReceivedReviews().add(this);
    }

    /**
     * [비즈니스 로직]
     * SOFT DELETE
     */
    public void softDelete(){
        //이미 삭제 되었는지 확인
        validateSoftDelete();

        this.deleteStatus=DeleteStatus.DELETED;
        this.reviewee.minusReviewedCount();
        this.reviewee.getStore().minusReviewedCount();

    }

    /**
     * [비즈니스 로직]
     * RECOVERY
     */
    public void recovery(){

        //이미 삭제상태가 아닌지 확인
        validateRecovery();

        this.deleteStatus=DeleteStatus.UNDELETED;
        this.reviewee.plusReviewedCount();
        this.reviewee.getStore().plusReviewedCount();

    }



    //==이미 삭제상태가 아닌지 확인하는 로직==//
    private void validateRecovery() {
        if(this.deleteStatus.equals(DeleteStatus.UNDELETED)){
            throw new ReviewCustomException(ErrorMessage.CANNOT_RECOVERY);
        }
    }

    //==이미 삭제 되었는지 확인하는 로직==//
    private void validateSoftDelete() {
        if (DeleteStatus.DELETED.equals(deleteStatus)){
            throw new ReviewCustomException(ErrorMessage.ALREADY_DELETE);
        }
    }


}



