package gmart.gmart.dto.member;

import gmart.gmart.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 정보 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateMemberInfoRequestDto {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name; //이름

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname; //닉네임

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone; //전화번호

    @Valid
    AddressDto address; //주소

    private String profileImageUrl; //프로필 이미지

}
