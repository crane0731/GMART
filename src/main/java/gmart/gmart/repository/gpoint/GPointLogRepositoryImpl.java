package gmart.gmart.repository.gpoint;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.domain.log.GPointLog;
import gmart.gmart.domain.log.QGPointLog;
import gmart.gmart.dto.gpoint.SearchGPointLogCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 사용자 건포인트 거래 로그 레파지토리 구현체 클래스
 */
public class GPointLogRepositoryImpl implements GPointLogRepositoryCustom {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public GPointLogRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<GPointLog> findAllByCond(Member member, SearchGPointLogCondDto cond, Pageable pageable) {

        QGPointLog gPointLog = QGPointLog.gPointLog;
        BooleanBuilder builder = new BooleanBuilder();


        builder.and(gPointLog.member.eq(member));


        builder.and(gPointLog.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(cond.getYear() != null &&!cond.getYear().isBlank()) {
            Integer year = Integer.parseInt(cond.getYear());

            builder.and(
                    Expressions.numberTemplate(Integer.class, "YEAR({0})", gPointLog.createdDate)
                            .eq(year)
            );
        }

        if(cond.getGMoneyDeltaType()!=null) {
            builder.and(gPointLog.gPointDeltaType.eq(cond.getGMoneyDeltaType()));
        }


        List<GPointLog> content = query
                .select(gPointLog)
                .from(gPointLog)
                .join(gPointLog.member).fetchJoin()
                .join(gPointLog.order).fetchJoin()
                .join(gPointLog.order.item).fetchJoin()
                .where(builder)
                .orderBy(gPointLog.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = query.select(gPointLog.count())
                .from(gPointLog)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }




}
