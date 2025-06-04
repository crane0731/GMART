package gmart.gmart.repository.favorite;

import gmart.gmart.domain.FavoriteGundam;
import gmart.gmart.domain.Gundam;
import gmart.gmart.domain.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 회원 관심 건담 레파지토리
 */
public interface FavoriteGundamRepository extends JpaRepository<FavoriteGundam, Long>,FavoriteGundamRepositoryCustom {

    /**
     * 회원과 건담정보를 통해 회원 관심 건담이 이미 존재하는지 확인
     * @param member 회원 엔티티
     * @param gundam 건담 엔티티
     * @return Boolean
     */
    @Query("SELECT CASE WHEN count(fg)>0 THEN TRUE ELSE FALSE END  " +
            "FROM FavoriteGundam fg " +
            "where fg.member=:member and fg.gundam=:gundam")
    Boolean existsByMemberAndGundam(@Param("member") Member member, @Param("gundam") Gundam gundam);

}
