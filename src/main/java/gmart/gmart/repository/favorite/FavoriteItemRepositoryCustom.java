package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteItem;
import gmart.gmart.domain.Member;

import java.util.List;

/**
 * 커스텀 관심 상품 레파지토리
 */
public interface FavoriteItemRepositoryCustom {

    /**
     * 회원 + 검색 조건에 따라 관심 상품 리스트 조회
     * @param member 회원 엔티티
     * @param itemTitle 상품 제목
     * @return List<FavoriteItem> 관심 상품 엔티티 리스트
     */
    List<FavoriteItem> findByCond(Member member, String itemTitle);
}
