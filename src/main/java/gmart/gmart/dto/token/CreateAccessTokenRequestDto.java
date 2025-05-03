package gmart.gmart.dto.token;

import lombok.Getter;
import lombok.Setter;

/**
 * 엑세스 토큰 생성 요청 DTO
 */
@Getter
@Setter
public class CreateAccessTokenRequestDto {
    private String refreshToken;
}
