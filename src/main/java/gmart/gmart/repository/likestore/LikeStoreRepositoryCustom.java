package gmart.gmart.repository.likestore;

import gmart.gmart.domain.LikeStore;
import gmart.gmart.domain.Member;
import gmart.gmart.dto.store.SearchLikeStoreCondDto;

import java.util.List;

/**
 * 커스텀 상점 좋아요 레파지토리
 */
public interface LikeStoreRepositoryCustom {

    /**
     * 회원과 검색 조건에 따라 상점 좋아요 엔티티 리스트 조회
     * @param member 회원 엔티티
     * @param cond 검색 조건 DTO
     * @return List<LikeStore> 상점 좋아요 엔티티 리스트
     */
    List<LikeStore> findAllByCond(Member member , SearchLikeStoreCondDto cond);

}
