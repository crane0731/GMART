package gmart.gmart.dto.token;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

/**
 * 리프레쉬 토큰 + 엑세스 토큰 응답 DTO
 */
@Getter
@Setter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    /**
     * DTO 생성 메서드
     */
    public static TokenResponseDto createDto(String accessToken, String refreshToken) {
        TokenResponseDto dto = new TokenResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }
}
