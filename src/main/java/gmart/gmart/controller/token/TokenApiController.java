package gmart.gmart.controller.token;

import gmart.gmart.dto.token.CreateAccessTokenRequestDto;
import gmart.gmart.dto.token.CreateAccessTokenResponseDto;
import gmart.gmart.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 새로운 엑세스 토큰 생성 API
 */
@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    /**
     * 새로운 엑세스 토큰 생성
     * @param requestDto
     * @return
     */
    @PostMapping("/api/gmart/token")
    public ResponseEntity<CreateAccessTokenResponseDto> createNewAccessToken(@RequestBody CreateAccessTokenRequestDto requestDto) {
        String newAccessToken = tokenService.createNewAccessToken(requestDto.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponseDto(newAccessToken));
    }

}
