package gmart.gmart.repository.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QReview;
import gmart.gmart.domain.Review;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.enums.ReviewRole;
import gmart.gmart.dto.review.SearchReviewCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 커스텀 리뷰 레파지토리 구현체
 */
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ReviewRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * [동적 쿼리 메서드]
     * 조건에 따라 리뷰 리스트 조회 + 페이징
     * @param member  회원 엔티티
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<Review> 페이징 된 리뷰 엔티티
     */
    @Override
    public Page<Review> findByCondByMember(Member member, SearchReviewCondDto cond, Pageable pageable) {

        QReview review = QReview.review;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(review.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(cond.getReviewRole().equals(ReviewRole.REVIEWER)){
            builder.and(review.reviewer.eq(member));

        }else if(cond.getReviewRole().equals(ReviewRole.REVIEWEE)) {
            builder.and(review.reviewee.in(member));
        }

        if(cond.getReviewType()!=null){
            builder.and(review.reviewType.eq(cond.getReviewType()));
        }

        OrderSpecifier<LocalDateTime> order =
                cond.getCreatedDateSortType() == CreatedDateSortType.CREATE_DATE_ASC ? review.createdDate.asc() : review.createdDate.desc();


        List<Review> content = query
                .select(review)
                .from(review)
                .join(review.reviewer).fetchJoin()
                .join(review.reviewee).fetchJoin()
                .join(review.order).fetchJoin()
                .join(review.order.item).fetchJoin()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(review.count())
                .from(review)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
