package gmart.gmart.service.token;

import gmart.gmart.config.jwt.TokenProvider;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.RefreshToken;
import gmart.gmart.domain.enums.TokenType;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.JwtCustomException;
import gmart.gmart.repository.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * 리프레쉬 토큰 서비스
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    private final Duration refreshTokenValidity = Duration.ofDays(1);  // Refresh Token 유효 시간 (1일)

    public RefreshToken findByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId).orElseThrow(()->new JwtCustomException(ErrorMessage.NOT_FOUND_REFRESH_TOKEN));
    }


    /**
     * 회원을 통해 리프레쉬 토큰 삭제
     * @param member
     */
    @Transactional
    public void delete(Member member) {
        RefreshToken refreshToken = findByMemberId(member.getId());
        refreshTokenRepository.delete(refreshToken);
    }

    /**
     * 리프레쉬 토큰 조회
     * @param refreshToken
     * @return
     */
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new JwtCustomException(ErrorMessage.NOT_FOUND_REFRESH_TOKEN));
    }


    /**
     * 리프레쉬 토큰 생성 , 저장
     * @param member
     */
    @Transactional
    public String createRefreshToken(Member member) {

        //회원 아이디를 통해 이미 리프레쉬 토큰이 있는지 확인
        checkOldRefreshToken(member);

        //리프레쉬 토큰 생성
        String token = tokenProvider.generateToken(member, refreshTokenValidity, TokenType.REFRESH);

        //리프레쉬 토큰 엔티티 객체 생성
        RefreshToken refreshToken = RefreshToken.createEntity(member.getId(), token);

        //리프레쉬 토큰 엔티티 DB 저장
        refreshTokenRepository.save(refreshToken);

        return token;

    }

    //==회원 아이디를 통해 이미 리프레쉬 토큰이 있는지 확인==//
    private void checkOldRefreshToken(Member member) {
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(member.getId()).orElse(null);

        //이미 존재한다면 삭제
        if (savedToken != null) {
            refreshTokenRepository.delete(savedToken);
        }
    }
}
