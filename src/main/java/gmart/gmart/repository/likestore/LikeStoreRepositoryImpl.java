package gmart.gmart.repository.likestore;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.LikeStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QLikeStore;
import gmart.gmart.dto.store.SearchLikeStoreCondDto;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * 커스텀 상점 좋아요 레파지토리 구현체 클래스
 */
public class LikeStoreRepositoryImpl implements LikeStoreRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public LikeStoreRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 회원 + 검색 조건에 따라 상점 좋아요 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return
     */
    @Override
    public List<LikeStore> findAllByCond(Member member, SearchLikeStoreCondDto cond) {

        QLikeStore likeStore = QLikeStore.likeStore;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(likeStore.member.eq(member));

        if(cond.getName() != null && !cond.getName().isBlank()) {
            builder.and(likeStore.store.name.containsIgnoreCase(cond.getName()));
        }

        return query.select(likeStore)
                .from(likeStore)
                .where(builder)
                .orderBy(likeStore.createdDate.desc())
                .fetch();


    }
}
