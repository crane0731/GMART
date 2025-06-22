package gmart.gmart.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인에 필요한 요청 DTO
 */
@Slf4j
@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message="비밀번호를 입력해주세요.")
    private String password;

    /**
     * [생성 메서드]
     * 카카오 로그인을 위한 생성 메서드
     * @param loginId 로그인 아이디
     * @param password 비밀 번호
     * @return LoginRequestDto 로그인 요청 DTO
     */
    public static LoginRequestDto createForKakao(String loginId, String password){
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setLoginId(loginId);
        loginRequestDto.setPassword(password);
        return loginRequestDto;
    }

}
