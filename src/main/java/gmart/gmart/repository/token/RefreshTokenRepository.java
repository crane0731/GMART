package gmart.gmart.repository.token;

import gmart.gmart.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 회원 아이디로 리프레쉬 토큰 조회
     * @param memberId
     * @return
     */
    Optional<RefreshToken> findByMemberId(Long memberId);


    /**
     * 리프레쉬 토큰으로 조회
     * @param refreshToken
     * @return
     */
    Optional<RefreshToken> findByRefreshToken(String refreshToken);


}
