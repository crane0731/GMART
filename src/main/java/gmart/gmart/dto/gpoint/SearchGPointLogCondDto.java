package gmart.gmart.dto.gpoint;


import gmart.gmart.domain.enums.GPointDeltaType;
import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 거래 로그 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGPointLogCondDto {

    private String year;//년도

    private GPointDeltaType gMoneyDeltaType; //건포인트 변화 타입


    /**
     * [생성 메서드]
     * @param year 년도
     * @param gPointDeltaType 건포인트 변화 타입
     * @return
     */
    public static SearchGPointLogCondDto create(String year, GPointDeltaType gPointDeltaType) {
        SearchGPointLogCondDto dto = new SearchGPointLogCondDto();
        dto.setYear(year);
        dto.setGMoneyDeltaType(gPointDeltaType);
        return dto;
    }

}
