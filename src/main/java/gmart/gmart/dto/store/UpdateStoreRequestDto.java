package gmart.gmart.dto.store;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 상점 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateStoreRequestDto {

    @NotBlank(message = "상점 이름을 입력해주세요.")
    private String name; //상점 이름

    @NotBlank(message = "상점 소개글을 입력해주세요.")
    private String introduction; //상점 소개
    private String imageUrl;//이미지 URL
}
