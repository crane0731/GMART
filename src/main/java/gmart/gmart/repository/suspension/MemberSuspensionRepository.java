package gmart.gmart.repository.suspension;

import gmart.gmart.domain.MemberSuspension;
import gmart.gmart.domain.enums.MemberActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원 계정 일시 정지 레파지토리
 */
public interface MemberSuspensionRepository extends JpaRepository<MemberSuspension, Long> {


    /**
     * 이미 회원 계정이 정지중인지 확인
     * @param memberId
     * @param now
     * @return boolean
     */
    @Query("SELECT CASE WHEN COUNT(ms) > 0 THEN true ELSE false END " +
            "FROM MemberSuspension ms " +
            "WHERE ms.member.id = :memberId AND ms.memberActiveStatus = :status AND ms.endAt > :now")
    boolean existsActiveSuspension(@Param("memberId") Long memberId, @Param("now") LocalDateTime now, @Param("status")MemberActiveStatus status);


    /**
     * 계정 정지 만료일이 지난 회원들을 조회
     * @param now 현재 시각
     * @return 만료된 회원 정지 엔티티 리스트
     */
    @Query("SELECT ms FROM MemberSuspension ms WHERE ms.endAt < :now")
    List<MemberSuspension> findExpired(@Param("now") LocalDateTime now);


    /**
     * 회원 아이디로 조회
     * @param memberId 회원 아이디
     * @return List<MemberSuspension>
     */
    List<MemberSuspension> findByMemberId(@Param("memberId") Long memberId);

}
