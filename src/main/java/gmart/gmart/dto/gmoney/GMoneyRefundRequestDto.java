package gmart.gmart.dto.gmoney;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 환불 요청 DTO
 */
@Getter
@Setter
public class GMoneyRefundRequestDto {

    @NotNull(message = "환불 할 가격을 입력해주세요")
    private Long price; //가격
}
