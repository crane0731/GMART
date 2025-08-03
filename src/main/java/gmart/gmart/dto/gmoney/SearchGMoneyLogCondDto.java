package gmart.gmart.dto.gmoney;

import gmart.gmart.domain.enums.GMoneyDeltaType;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 거래 로그 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGMoneyLogCondDto {

    private String year;//년도
    private GMoneyDeltaType gMoneyDeltaType; //건머니 변화 타입

    /**
     * [생성 메서드]
     * @param year 연도
     * @param gMoneyDeltaType 건머니 변화 타입
     * @return SearchGMoneyLogCondDto
     */
    public static SearchGMoneyLogCondDto create(String year, GMoneyDeltaType gMoneyDeltaType) {
        SearchGMoneyLogCondDto dto = new SearchGMoneyLogCondDto();
        dto.setYear(year);
        dto.setGMoneyDeltaType(gMoneyDeltaType);
        return dto;
    }
}
