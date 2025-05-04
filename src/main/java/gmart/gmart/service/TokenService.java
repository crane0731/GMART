package gmart.gmart.service;

import gmart.gmart.config.jwt.TokenProvider;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.enums.TokenType;
import gmart.gmart.exception.CustomException;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.JwtCustomException;
import gmart.gmart.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * 토큰 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    /**
     * 리프레쉬 토큰을 통해 새로운 엑세스 토큰을 생성
     * @param refreshToken
     * @return
     */
    public String createNewAccessToken(String refreshToken) {

        //토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validRefreshToken(refreshToken)) {
            throw new JwtCustomException(ErrorMessage.INVALID_TOKEN);
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorMessage.NOT_FOUND_MEMBER));

        return tokenProvider.generateToken(member, Duration.ofHours(2), TokenType.ACCESS);

    }




}
