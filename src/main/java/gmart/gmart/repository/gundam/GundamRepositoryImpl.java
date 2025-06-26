package gmart.gmart.repository.gundam;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.QGundam;
import gmart.gmart.dto.gundam.SearchGundamCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 커스텀 건담 정보 레파지토리 구현체 클래스
 */
public class GundamRepositoryImpl implements GundamRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public GundamRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     *조건에 따라 건담 리스트를 검색하는 동적 쿼리 메서드
     * @param cond 건담 검색 조건
     * @return List<Gundam> 건담 엔티티 리스트
     */
    @Override
    public Page<Gundam> findAllByCond(SearchGundamCondDto cond, Pageable pageable) {

        QGundam gundam = QGundam.gundam;

        BooleanBuilder builder = new BooleanBuilder();

        if(cond.getName()!=null && !cond.getName().isBlank()) {
            builder.and(gundam.name.containsIgnoreCase(cond.getName()));
        }

        if(cond.getGrade()!=null){
            builder.and(gundam.grade.eq(cond.getGrade()));
        }

        List<Gundam> content = query.select(gundam)
                .from(gundam)
                .where(builder)
                .orderBy(gundam.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(gundam.count())
                .from(gundam)
                .where(builder)
                .fetchOne();


        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
