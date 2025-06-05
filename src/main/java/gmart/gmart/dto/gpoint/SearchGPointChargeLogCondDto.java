package gmart.gmart.dto.gpoint;

import gmart.gmart.domain.enums.ChargeType;
import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 충전 로그 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGPointChargeLogCondDto {

    private String year; //년도
    private ChargeType chargeType;//충전 타입
}
