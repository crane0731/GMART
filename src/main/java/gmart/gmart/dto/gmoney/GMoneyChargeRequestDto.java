package gmart.gmart.dto.gmoney;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 충전 요청 DTO
 */
@Getter
@Setter
public class GMoneyChargeRequestDto {

    @NotNull(message = "충전 할 가격을 입력해주세요")
    private Long price; //가격

}
