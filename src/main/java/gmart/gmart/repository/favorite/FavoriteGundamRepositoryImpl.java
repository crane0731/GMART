package gmart.gmart.repository.favorite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QFavoriteGundam;
import gmart.gmart.dto.favorite.SearchFavoriteGundamCondDto;
import jakarta.persistence.EntityManager;

import java.util.List;

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
    public List<FavoriteGundam> findAllByMemberAndCond(SearchFavoriteGundamCondDto cond, Member member) {
        QFavoriteGundam favoriteGundam = QFavoriteGundam.favoriteGundam;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(favoriteGundam.member.eq(member));

        if(cond.getName()!=null && !cond.getName().isBlank()){
            builder.and(favoriteGundam.gundam.name.contains(cond.getName()));
        }

        if(cond.getGrade()!=null){
            builder.and(favoriteGundam.gundam.grade.eq(cond.getGrade()));
        }

        return   query
                .select(favoriteGundam)
                .from(favoriteGundam)
                .where(builder)
                .orderBy(favoriteGundam.createdDate.desc())
                .fetch();
    }
}
