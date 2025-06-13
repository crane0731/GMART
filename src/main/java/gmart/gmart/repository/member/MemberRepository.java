package gmart.gmart.repository.member;

import gmart.gmart.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 회원 리포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 로그인아이디로 회원 조회
     */
    Optional<Member> findByLoginId(String loginId);


    /**
     * 닉네임으로 회원 조회
     */
    Optional<Member> findByNickname(String nickname);

    /**
     * 전화번호로 회운 조회
     */
    Optional<Member> findByPhoneNumber(String phoneNumber);

}
