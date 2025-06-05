package gmart.gmart.dto.gpoint;

import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 회수 요청 DTO
 */
@Getter
@Setter
public class GPointRefundRequestDto {

    private Long point; //회수 금액
}
