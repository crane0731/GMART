package gmart.gmart.service;

import gmart.gmart.domain.RefreshToken;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.JwtCustomException;
import gmart.gmart.repository.RefreshTokenRepository;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레쉬 토큰 서비스
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 리프레쉬 토큰 조회
     * @param refreshToken
     * @return
     */
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new JwtCustomException(ErrorMessage.NOT_FOUND_REFRESH_TOKEN));
    }
}
