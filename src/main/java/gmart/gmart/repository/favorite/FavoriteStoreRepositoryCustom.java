package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteStore;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.favorite.SearchFavoriteStoreCondDto;

import java.util.List;

/**
 * 커스텀 관심 상점 레파지토리
 */
public interface FavoriteStoreRepositoryCustom {

    /**
     * 검색 조건에 따라 관심 상점 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return List<FavoriteStore> 관심상점 엔티티 리스트
     */
    List<FavoriteStore> findAllByCond(Member member , SearchFavoriteStoreCondDto cond);

}
