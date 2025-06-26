package gmart.gmart.repository.favorite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QFavoriteGundam;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.hibernate.query.results.Builders.fetch;

/**
 * 관심 건담 커스텀 레파지토리 구현
 */
public class FavoriteGundamRepositoryImpl implements FavoriteGundamRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FavoriteGundamRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     *조건에 따라 관심 건담 리스트를 검색하는 동적 쿼리 메서드
     * @param cond 관심 건담 검색 조건
     * @return List<FavoriteGundam> 관심 건담 엔티티 리스트
     */
    @Override
    public Page<FavoriteGundam> findAllByMemberAndCond(SearchFavoriteGundamCondDto cond, Member member, Pageable pageable) {
        QFavoriteGundam favoriteGundam = QFavoriteGundam.favoriteGundam;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(favoriteGundam.member.eq(member));

        builder.and(favoriteGundam.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(cond.getName()!=null && !cond.getName().isBlank()){
            builder.and(favoriteGundam.gundam.name.containsIgnoreCase(cond.getName()));
        }

        if(cond.getGrade()!=null){
            builder.and(favoriteGundam.gundam.grade.eq(cond.getGrade()));
        }

        List<FavoriteGundam> content = query
                .select(favoriteGundam)
                .from(favoriteGundam)
                .where(builder)
                .orderBy(favoriteGundam.createdDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = query.select(favoriteGundam.count())
                .from(favoriteGundam)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }
}
