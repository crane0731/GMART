package gmart.gmart.repository.gmoney;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.domain.log.QGMoneyChargeLog;
import gmart.gmart.dto.gmoney.SearchGMoneyChargeLogCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 사용자 건머니 충전 로그 레파지토리 구현체 클래스
 */
public class GMoneyChargeLogRepositoryImpl implements GMoneyChargeLogRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public GMoneyChargeLogRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * [동적 쿼리 메서드]
     * 회원과 검색 조건에 따라 건머니 충전 로그 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return  List<GMoneyChargeLog> 건머니 차지로그 엔티티 리스트
     */
    @Override
    public Page<GMoneyChargeLog> findAllByCond(Member member, SearchGMoneyChargeLogCondDto cond, Pageable pageable) {
        QGMoneyChargeLog gMoneyChargeLog = QGMoneyChargeLog.gMoneyChargeLog;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(gMoneyChargeLog.memberId.eq(member.getId()));

        builder.and(gMoneyChargeLog.deleteStatus.eq(DeleteStatus.UNDELETED));


        if(cond.getYear() != null &&!cond.getYear().isBlank()) {
            Integer year = Integer.parseInt(cond.getYear());

            builder.and(
                    Expressions.numberTemplate(Integer.class, "YEAR({0})", gMoneyChargeLog.createdDate)
                            .eq(year)
            );
        }

        if(cond.getChargeType()!=null){
            builder.and(gMoneyChargeLog.chargeType.eq(cond.getChargeType()));
        }

        List<GMoneyChargeLog> content = query
                .select(gMoneyChargeLog)
                .from(gMoneyChargeLog)
                .where(builder)
                .orderBy(gMoneyChargeLog.createdDate.desc())
                .fetch();

        Long total = query.select(gMoneyChargeLog.count())
                .from(gMoneyChargeLog)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
