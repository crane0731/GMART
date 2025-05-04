package gmart.gmart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인에 필요한 요청 DTO
 */
@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message="비밀번호를 입력해주세요.")
    private String password;

}
