package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteStore;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.favorite.SearchFavoriteStoreCondDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 커스텀 관심 상점 레파지토리
 */
public interface FavoriteStoreRepositoryCustom {

    /**
     * 검색 조건에 따라 관심 상점 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @param pageable 페이징
     * @return Page<FavoriteStore> 페이징된 관심 상점 엔티티 리스트
     */
    Page<FavoriteStore> findAllByCond(Member member , SearchFavoriteStoreCondDto cond, Pageable pageable);

}
