package gmart.gmart.controller.token;

import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.token.CreateAccessTokenRequestDto;
import gmart.gmart.dto.token.CreateAccessTokenResponseDto;
import gmart.gmart.service.token.TokenService;
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
    public ResponseEntity<ApiResponse<?>> createNewAccessToken(@RequestBody CreateAccessTokenRequestDto requestDto) {

        //새로운 엑세스 토큰 생성
        String newAccessToken = tokenService.createNewAccessToken(requestDto.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new CreateAccessTokenResponseDto(newAccessToken)));
    }

    /**
     * 테스트용 api
     * @return
     */
    @PostMapping("/api/hello")
    public ResponseEntity<ApiResponse<?>> hello() {
        return ResponseEntity.ok(ApiResponse.success("Hello World!"));
    }

}
