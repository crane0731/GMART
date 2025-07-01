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


    /**
     * [생성 메서드]
     * @param year 년도
     * @param chargeType 충전 타입
     * @return SearchGPointChargeLogCondDto 검색 조건 DTO
     */
    public static SearchGPointChargeLogCondDto create(String year, ChargeType chargeType) {

        SearchGPointChargeLogCondDto dto = new SearchGPointChargeLogCondDto();
        dto.setYear(year);
        dto.setChargeType(chargeType);
        return dto;
    }
}
