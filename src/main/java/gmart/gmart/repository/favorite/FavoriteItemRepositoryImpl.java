package gmart.gmart.repository.favorite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gmart.gmart.domain.FavoriteItem;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.QFavoriteItem;
import gmart.gmart.domain.enums.DeleteStatus;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * 커스텀 관심 상품 레파지토리 구현체 클래스
 */
public class FavoriteItemRepositoryImpl implements FavoriteItemRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FavoriteItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * 검색 조건에 따라 현재 로그인한 회원의 관심 상품 리스트 조회
     * @param member 회원 엔티티
     * @param itemTitle 상품 제목
     * @return List<FavoriteItem> 상품 엔티티 리스트
     */
    @Override
    public List<FavoriteItem> findByCond(Member member, String itemTitle) {

        QFavoriteItem favoriteItem = QFavoriteItem.favoriteItem;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(favoriteItem.member.id.eq(member.getId()));
        builder.and(favoriteItem.deleteStatus.eq(DeleteStatus.UNDELETED));

        if(itemTitle!=null && ! itemTitle.isBlank()){
            builder.and(favoriteItem.item.title.containsIgnoreCase(itemTitle));
        }

        return query
                .select(favoriteItem)
                .from(favoriteItem)
                .where(builder)
                .orderBy(favoriteItem.createdDate.asc())
                .fetch();

    }

}
