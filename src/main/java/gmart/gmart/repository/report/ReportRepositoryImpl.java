package gmart.gmart.repository.report;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.QReport;
import gmart.gmart.domain.Report;
import gmart.gmart.dto.enums.CreatedDateSortType;
import gmart.gmart.dto.report.SearchReportCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ReportRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * 검색 조건에 따라 신고 목록 리스트 조회
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<Report> 페이징된 신고 엔티티
     */
    @Override
    public Page<Report> findAllByCond(SearchReportCondDto cond, Pageable pageable) {

        QReport report = QReport.report;
        BooleanBuilder builder= new BooleanBuilder();

        if(cond.getStatus()!=null) {
            builder.and(report.reportStatus.eq(cond.getStatus()));
        }

        if(cond.getReporterRole()!=null) {
            builder.and(report.reporterRole.eq(cond.getReporterRole()));
        }

        OrderSpecifier<LocalDateTime> order =
                cond.getCreatedDateSortType() == CreatedDateSortType.CREATE_DATE_ASC ? report.createdDate.asc() : report.createdDate.desc();

        List<Report> content = query
                .select(report)
                .from(report)
                .join(report.reporter).fetchJoin()
                .join(report.reportedMember).fetchJoin()
                .join(report.item).fetchJoin()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(report.count())
                .from(report)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
