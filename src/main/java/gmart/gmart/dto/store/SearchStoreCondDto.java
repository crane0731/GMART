package gmart.gmart.dto.store;

import gmart.gmart.dto.enums.StoreSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 상점 검색 조건 DTO
 */
@Getter
@Setter
public class SearchStoreCondDto {

    private String name; //상점 이름
    private StoreSortType sortType; //정렬 조건 , [HIGH_REVIEW,HIGH_LIKED_COUNT]


    /**
     * [생성 메서드]
     * @param name 상점 이름
     * @param sortType 정렬 조건
     * @return SearchStoreCondDto 검색 조건 DTO
     */
    public static SearchStoreCondDto create(String name, StoreSortType sortType) {
        SearchStoreCondDto dto = new SearchStoreCondDto();
        dto.setName(name);
        dto.setSortType(sortType);
        return dto;
    }

}
