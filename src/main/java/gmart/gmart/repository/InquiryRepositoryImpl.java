package gmart.gmart.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Inquiry;
import gmart.gmart.domain.QInquiry;
import gmart.gmart.dto.enums.CreateDateSortType;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 커스텀 문의 레파지토리 구현체 클래스
 */
@Repository
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public InquiryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query=new JPAQueryFactory(em);
    }


    /**
     * 조건에 따라 문의 리스트를 검색하는 동적 쿼리 메서드
     * @param cond 검색 조건
     * @return 문의 엔티티 리스트
     */
    @Override
    public Page<Inquiry> findAllByCond(SearchInquiryCondDto cond, Pageable pageable) {

        QInquiry inquiry = QInquiry.inquiry;

        BooleanBuilder builder = new BooleanBuilder();

        if(cond.getTitle() != null && !cond.getTitle().isBlank()) {
            builder.and(inquiry.title.containsIgnoreCase(cond.getTitle()));
        }

        if(cond.getAnswerStatus() != null) {
            builder.and(inquiry.answerStatus.eq(cond.getAnswerStatus()));
        }

        OrderSpecifier<LocalDateTime> order =
                cond.getCreateSortType() == CreateDateSortType.CREATE_DATE_ASC ? inquiry.createdDate.asc() : inquiry.createdDate.desc();

        //실제 페이지 데이터 조회
        List<Inquiry> content = query
                .select(inquiry)
                .from(inquiry)
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 개수 조회(페이징을 위해 필요)
        Long total = query
                .select(inquiry.count())
                .from(inquiry)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

}
