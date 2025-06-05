package gmart.gmart.repository.gpoint;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.domain.log.QGPointChargeLog;
import gmart.gmart.dto.gpoint.SearchGPointChargeLogCondDto;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * 커스텀 건포인트 충전 로그 구현체 클래스
 */
public class GPointChargeLogRepositoryImpl implements GPointChargeLogRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public GPointChargeLogRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * [동적 쿼리 메서드]
     * 회원과 검색 조건에 따라 건포인트 충전 로그 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return List<GPointChargeLog> 건포인트 충전 로그 엔티티 리스트
     */
    @Override
    public List<GPointChargeLog> findAllByCond(Member member, SearchGPointChargeLogCondDto cond) {
        QGPointChargeLog gPointChargeLog = QGPointChargeLog.gPointChargeLog;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(gPointChargeLog.memberId.eq(member.getId()));

        if(cond.getYear() != null && !cond.getYear().isBlank()) {
            Integer year = Integer.parseInt(cond.getYear());

            builder.and(
                    Expressions.numberTemplate(Integer.class, "YEAR({0})", gPointChargeLog.createdDate)
                            .eq(year)
            );
        }

        if(cond.getChargeType()!=null){
            builder.and(gPointChargeLog.chargeType.eq(cond.getChargeType()));
        }

        return   query
                .select(gPointChargeLog)
                .from(gPointChargeLog)
                .where(builder)
                .orderBy(gPointChargeLog.createdDate.desc())
                .fetch();

    }
}
