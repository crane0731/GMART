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


    /**
     * [생성 메서드]
     * @param year 년도
     * @param chargeType 충전 타입
     * @return SearchGMoneyChargeLogCondDto 검색 조건 DTO
     */
    public static SearchGMoneyChargeLogCondDto create(String year, ChargeType chargeType) {
        SearchGMoneyChargeLogCondDto dto = new SearchGMoneyChargeLogCondDto();
        dto.setYear(year);
        dto.setChargeType(chargeType);
        return dto;
    }
}
