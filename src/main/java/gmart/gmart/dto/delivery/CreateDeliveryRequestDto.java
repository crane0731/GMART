package gmart.gmart.dto.delivery;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 배송 생성 요청 DTO
 */
@Getter
@Setter
public class CreateDeliveryRequestDto {

    @NotBlank(message = "송장 번호를 입력해주세요.")
    private String trackingNumber; //송장 번호
}
