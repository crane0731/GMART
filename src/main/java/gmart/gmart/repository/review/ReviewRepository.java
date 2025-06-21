package gmart.gmart.repository.review;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.Review;
import gmart.gmart.domain.enums.DeleteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 리뷰 레파지토리
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * [쿼리 메서드]
     * 다음 조합으로 단건 조회
     * @param reviewer  리뷰어
     * @param reviewee 리뷰 대상
     * @param order 주문
     * @return Review 엔티티
     */
    Optional<Review> findByReviewerAndRevieweeAndOrder(Member reviewer, Member reviewee, Order order);
}
