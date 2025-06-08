package gmart.gmart.repository.likestore;

import gmart.gmart.domain.LikeStore;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
     * 상점 좋아요 레파지토리
     */
    public interface LikeStoreRepository extends JpaRepository<LikeStore, Long> {

        /**
         * 회원과 상점으로 이미 상점 좋아요 테이블이 있는지 확인
         * @param member 회원 엔티티
         * @param store 상점 엔티티
         * @return Boolean
         */
        boolean existsByMemberAndStore(Member member, Store store);


    /**
     * 회원과 상점으로 좋아요 상점 찾기
     * @param member 회원 엔티티
     * @param store 상점 엔티티
     * @return  Optional<LikeStore>  좋아요 상점 엔티티
     */
    Optional<LikeStore> findByMemberAndStore(Member member, Store store);
    }
