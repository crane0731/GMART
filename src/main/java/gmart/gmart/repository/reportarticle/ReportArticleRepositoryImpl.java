package gmart.gmart.repository.reportarticle;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.QReportArticle;
import gmart.gmart.domain.ReportArticle;
import gmart.gmart.dto.enums.CreateDateSortType;
import gmart.gmart.dto.reportarticle.SearchReportArticleCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 신고  레파지토리 구현체 클래스
 */
@Repository
public class ReportArticleRepositoryImpl implements ReportArticleRepositoryCustom {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public ReportArticleRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 조건에 따라 게시글 신고 리스트를 검색하는 동적 쿼리 메서드
     * @param cond 검색 조건
     * @return 문의 엔티티 리스트
     */
    @Override
    public Page<ReportArticle> findAllByCond(SearchReportArticleCondDto cond, Pageable pageable) {

        QReportArticle reportArticle = QReportArticle.reportArticle;

        BooleanBuilder builder = new BooleanBuilder();

        if(cond.getReportStatus() !=null){
            builder.and(reportArticle.status.eq(cond.getReportStatus()));
        }

        OrderSpecifier<LocalDateTime> order =
                cond.getCreateDateSortType() == CreateDateSortType.CREATE_DATE_ASC ? reportArticle.createdDate.asc() : reportArticle.createdDate.desc();

        //실제 페이지 데이터 조회
        List<ReportArticle> content = query
                .select(reportArticle)
                .from(reportArticle)
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 개수 조회(페이징을 위해 필요)
        Long total = query
                .select(reportArticle.count())
                .from(reportArticle)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

}
