package gmart.gmart.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 생성 요청 DTO
 */
@Getter
@Setter
public class CreateOrderRequestDto {

    @NotNull(message = "사용할 포인트를 입력해주세요.")
    private Long usedPoint; //사용 포인트


}
