package gmart.gmart.dto.login;

import gmart.gmart.dto.AddressDto;
import jakarta.validation.Valid;
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

    @Valid
    AddressDto address; //주소

    private String profileImageUrl; //프로필 이미지


    /**
     * [생성 메서드]
     * 카카오 회원가입을 위한 생성 메서드
     * @param loginId 로그인 아이디
     * @param nickname 닉네임
     * @param profileImageUrl 프로필 이미지 URL
     * @return SignUpRequestDto 회원 가입 요청 DTO
     */
    public static SignUpRequestDto createForKakao(String loginId, String nickname,String profileImageUrl){

        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setLoginId(loginId);
        dto.setName(nickname);
        dto.setNickname(nickname+"@Kakao");
        dto.setProfileImageUrl(profileImageUrl);
        dto.setPhone(null);
        dto.setAddress(null);
        dto.setPassword("0000");
        dto.setPasswordCheck("0000");

        return dto;

    }
}
