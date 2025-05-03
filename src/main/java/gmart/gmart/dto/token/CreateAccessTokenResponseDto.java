package gmart.gmart.dto.token;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 엑세스 토큰 생성 응답 DTO
 */
@Getter
@AllArgsConstructor
public class CreateAccessTokenResponseDto {

    private String accessToken;

}
