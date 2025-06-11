package gmart.gmart.repository.favorite;

import gmart.gmart.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 관심 상품 레파지토리
 */
public interface FavoriteItemRepository  extends JpaRepository<FavoriteItem, Long>,FavoriteItemRepositoryCustom {


    /**
     * 회원과 상품 조합으로 관심 상품 엔티티 조회
     * @param member 회원 엔티티
     * @param item 상품 엔티티
     * @return  Optional<FavoriteItem> 관심 상품 엔티티
     */
    Optional<FavoriteItem> findByMemberAndItem(Member member, Item item);


}
