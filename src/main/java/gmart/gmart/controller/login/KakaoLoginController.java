package gmart.gmart.controller.login;

import gmart.gmart.config.kakao.KakaoProperties;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.token.TokenResponseDto;
import gmart.gmart.service.kakao.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 카카오 로그인 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class KakaoLoginController {


    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService; //카카오 서비스

    //카카오 로그인 시작- 카카오 인증 페이지로 리다이렉트
    @GetMapping("/kakao")
    public ResponseEntity<ApiResponse<?>> kakaoLogin()throws IOException {

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id="+kakaoProperties.getClientId() +
                "&redirect_uri=http://localhost:8081/api/auth/kakao/callback" +
                "&response_type=code";

        return ResponseEntity.ok().body(ApiResponse.success(kakaoAuthUrl));
    }

    // 카카오 인증 콜백 처리

    /**
     * [컨트롤러]
     * 카카오 인증 콜백 처리
     * @param code 카카오 인증 코드
     */
    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

        TokenResponseDto responseDto = kakaoService.kakaoLogin(code);

        // 토큰을 쿼리 파라미터로 붙여서 프론트엔드 특정 페이지로 리다이렉트
        String redirectUrl = "http://localhost:5173/kakao-success"
                + "?accessToken=" + responseDto.getAccessToken()
                + "&refreshToken=" + responseDto.getRefreshToken();

        response.sendRedirect(redirectUrl);
    }

}
