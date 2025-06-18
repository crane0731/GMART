package gmart.gmart.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 취소 요청 DTO
 */
@Getter
@Setter
public class CancelOrderRequestDto {

    @NotBlank(message = "취소 사유를 입력해주세요.")
    private String cancelReason; //취소 사유

}
