package gmart.gmart.repository.token;

import gmart.gmart.domain.KakaoAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 카카오 엑세스 토큰 레파지토리
 */
public interface KakaoAccessTokenRepository extends JpaRepository<KakaoAccessToken, Long> {

    Optional<KakaoAccessToken> findByMemberId(Long id);

    @Query("SELECT k FROM KakaoAccessToken k WHERE k.member.id = :memberId")
    List<KakaoAccessToken> findAllByMemberId(Long memberId);

}


