package gmart.gmart.dto.password;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 비밀번호 변경 요청 DTO
 */
@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank(message = "이전 비밀번호를 입력해주세요.")
    private String oldPassword; //이전 비밀번호

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    private String newPassword; //새로운 비밀번호

    @NotBlank(message = "새로운 비밀번호 체크를 입력해주세요")
    private String newPasswordCheck;// 새로운 비밀번호 체크

}
