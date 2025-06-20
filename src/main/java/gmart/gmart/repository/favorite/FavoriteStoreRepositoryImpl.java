package gmart.gmart.repository.favorite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.FavoriteStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QFavoriteGundam;
import gmart.gmart.domain.QFavoriteStore;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.dto.favorite.SearchFavoriteStoreCondDto;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * 커스텀 관심 상점 레파지토리 구현체
 */
public class FavoriteStoreRepositoryImpl implements FavoriteStoreRepositoryCustom {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public FavoriteStoreRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 회원과 검색 조건에 따라 관심 상점 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return List<FavoriteStore> 관심 상점 엔티티 리스트
     */
    @Override
    public List<FavoriteStore> findAllByCond(Member member, SearchFavoriteStoreCondDto cond) {

        QFavoriteStore favoriteStore = QFavoriteStore.favoriteStore;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(favoriteStore.member.eq(member));

        builder.and(favoriteStore.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(cond.getName()!=null && !cond.getName().isBlank()) {
            builder.and(favoriteStore.store.name.containsIgnoreCase(cond.getName()));
        }

        return query.select(favoriteStore)
                .from(favoriteStore)
                .where(builder)
                .orderBy(favoriteStore.createdDate.desc())
                .fetch();

    }
}
