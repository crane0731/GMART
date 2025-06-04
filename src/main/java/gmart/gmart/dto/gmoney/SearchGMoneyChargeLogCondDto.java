package gmart.gmart.dto.gmoney;

import gmart.gmart.domain.enums.ChargeType;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 충전 로그 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGMoneyChargeLogCondDto {

    private String year; //년도
    private ChargeType chargeType;
}
