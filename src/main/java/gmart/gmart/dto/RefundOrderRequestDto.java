package gmart.gmart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 환불 요청 DTO
 */
@Getter
@Setter
public class RefundOrderRequestDto {

    @NotBlank(message = "취소 환불 사유를 입력해주세요.")
    private String refundReason; //환불 사유

}
