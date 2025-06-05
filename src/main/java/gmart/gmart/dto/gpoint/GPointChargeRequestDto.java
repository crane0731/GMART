package gmart.gmart.dto.gpoint;

import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 충전 요청 DTO
 */
@Getter
@Setter
public class GPointChargeRequestDto {

    private Long point; //충전 금액
    private String chargeType; //충전 타입
}
