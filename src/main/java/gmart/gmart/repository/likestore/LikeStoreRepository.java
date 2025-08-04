package gmart.gmart.repository.likestore;

import gmart.gmart.domain.LikeStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import gmart.gmart.domain.enums.DeleteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
     * 상점 좋아요 레파지토리
     */
    public interface LikeStoreRepository extends JpaRepository<LikeStore, Long>,LikeStoreRepositoryCustom {

        /**
         * 회원과 상점으로 이미 상점 좋아요 테이블이 있는지 확인
         * @param member 회원 엔티티
         * @param store 상점 엔티티
         * @return Boolean
         */
        @Query("SELECT (count(ls) > 0) " +
                "FROM LikeStore ls " +
                "where ls.member=:member and ls.store =:store and ls.deleteStatus= :deleteStatus")
        boolean existsByMemberAndStore(@Param("member") Member member, @Param("store") Store store, @Param("deleteStatus") DeleteStatus deleteStatus);


    /**
     * 회원과 상점으로 좋아요 상점 찾기
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return  Optional<LikeStore>  좋아요 상점 엔티티
     */
    Optional<LikeStore> findByMemberAndStore(Member member, Store store);
    }
