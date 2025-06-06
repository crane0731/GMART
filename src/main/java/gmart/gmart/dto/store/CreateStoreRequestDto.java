package gmart.gmart.dto.store;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 상점 생성 요청 DTO
 */
@Getter
@Setter
public class CreateStoreRequestDto {

    @NotBlank(message = "상점 이름을 입력해 주세요.")
    private String name; //상점 이름

    @NotBlank(message = "상점 설명을 입력해 주세요.")
    private String introduction; //상점 설명
    private String imageUrl; //상점 이미지

}
