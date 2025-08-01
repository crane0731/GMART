package gmart.gmart.controller.login;

import gmart.gmart.dto.login.LoginRequestDto;
import gmart.gmart.dto.login.SignUpRequestDto;
import gmart.gmart.dto.api.ApiResponse;
import gmart.gmart.dto.token.TokenResponseDto;
import gmart.gmart.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입, 로그인 , 로그아웃 관련 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gmart")
public class    LoginController {

    private final MemberService memberService;

    /**
     * 로그인 컨트롤러
     * @param loginRequestDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //로그인
        TokenResponseDto tokenResponseDto = memberService.loginMember(loginRequestDto);

        return ResponseEntity.ok().body(ApiResponse.success(tokenResponseDto));

    }


    /**
     * 로그아웃 컨트롤러
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {

        //로그아웃
        memberService.logout(request);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "로그아웃 성공")));

    }

    /**
     * 회원 가입 컨트롤러
     * @param requestDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody SignUpRequestDto requestDto , BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();


        //필드에러가 있는지 확인
        //오류 메시지가 존재하면 이를 반환
        if (errorCheck(bindingResult, errorMessages)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("입력값이 올바르지 않습니다.", errorMessages));
        }

        //회원가입
        memberService.signup(requestDto);

        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "회원가입 성공")));

    }



    //==필드에러가 있는지 확인하는 로직==//
    private boolean errorCheck(BindingResult bindingResult, Map<String, String> errorMessages) {
        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );

            return true;
        }
        return false;
    }

}
