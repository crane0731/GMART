package gmart.gmart.repository.token;

import gmart.gmart.domain.KakaoAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 카카오 엑세스 토큰 레파지토리
 */
public interface KakaoAccessTokenRepository extends JpaRepository<KakaoAccessToken, Long> {


    Optional<KakaoAccessToken> findByMemberId(Long id);
}
