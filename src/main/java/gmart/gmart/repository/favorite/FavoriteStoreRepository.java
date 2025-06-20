package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 관심 상점 레파지토리
 */
public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long>, FavoriteStoreRepositoryCustom {


    /**
     * 회원과 상점으로 관심 상점 엔티티 조회
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return  Optional<FavoriteStore> 관심 상점 엔티티
     */
    Optional<FavoriteStore> findByMemberAndStore(Member member, Store store);


}
