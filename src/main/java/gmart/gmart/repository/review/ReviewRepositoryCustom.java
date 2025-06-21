package gmart.gmart.repository.review;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Review;
import gmart.gmart.dto.review.SearchReviewCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 커스텀 리뷰 레파지토리
 */
public interface ReviewRepositoryCustom {

    /**
     * [동적 쿼리 메서드]
     * 조건에 따라 리뷰 리스트 조회 + 페이징
     * @param member  회원 엔티티
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<Review> 페이징 된 리뷰 엔티티
     */
    Page<Review> findByCondByMember(Member member , SearchReviewCondDto cond, Pageable pageable);

}
