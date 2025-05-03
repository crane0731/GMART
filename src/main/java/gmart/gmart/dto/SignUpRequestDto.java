package gmart.gmart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 가입 요청 DTO
 */
@Getter
@Setter
public class SignUpRequestDto {
    @NotBlank(message = "로그인 아이디를 입력해 주세요.")
    private String loginId;   //로그인 아이디

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password; //비밀번호

    @NotBlank(message = "비밀번호 검증을 입력해 주세요.")
    private String passwordCheck;  //비밀번호 검증

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name; //이름

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname; //닉네임

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone; //전화번호

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address; //주소

    @NotBlank(message = "상세주소를 입력해 주세요.")
    private String addressDetail; //상세주소

    @NotBlank(message = "우편번호를 입력해 주세요.")
    private String zipcode; //우편번호

    private String profileImageUrl; //프로필 이미지

}
