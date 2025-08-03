package gmart.gmart.repository.gmoney;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.domain.log.QGMoneyLog;
import gmart.gmart.dto.gmoney.SearchGMoneyLogCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static gmart.gmart.domain.log.QGMoneyChargeLog.gMoneyChargeLog;

/**
 * 사용자 건머니 거래 로그 레파지토리 구현체 클래스
 */
public class GMoneyLogRepositoryImpl implements GMoneyLogRepositoryCustom {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public GMoneyLogRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<GMoneyLog> findAllByCond(Member member, SearchGMoneyLogCondDto cond, Pageable pageable) {

        QGMoneyLog gMoneyLog = QGMoneyLog.gMoneyLog;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(gMoneyLog.member.eq(member));


        builder.and(gMoneyLog.deleteStatus.eq(DeleteStatus.UNDELETED));


        if(cond.getYear() != null &&!cond.getYear().isBlank()) {
            Integer year = Integer.parseInt(cond.getYear());

            builder.and(
                    Expressions.numberTemplate(Integer.class, "YEAR({0})", gMoneyLog.createdDate)
                            .eq(year)
            );
        }


        if(cond.getGMoneyDeltaType()!=null) {
            builder.and(gMoneyLog.gMoneyDeltaType.eq(cond.getGMoneyDeltaType()));
        }


        List<GMoneyLog> content = query
                .select(gMoneyLog)
                .from(gMoneyLog)
                .join(gMoneyLog.member).fetchJoin()
                .join(gMoneyLog.order).fetchJoin()
                .join(gMoneyLog.order.item).fetchJoin()
                .where(builder)
                .orderBy(gMoneyLog.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = query.select(gMoneyLog.count())
                .from(gMoneyLog)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
