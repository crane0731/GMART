package gmart.gmart.dto.store;

import lombok.Getter;
import lombok.Setter;

/**
 * 상점 좋아요 리스트 조회 검색 조건 DTO
 */
@Getter
@Setter
public class SearchLikeStoreCondDto {

    public String name; //상점 이름

    /**
     * [생성 메서드]
     * @param name 상점 이름
     * @return SearchLikeStoreCondDto 검색 조건 DTO
     */
    public static SearchLikeStoreCondDto create(String name) {
        SearchLikeStoreCondDto dto = new SearchLikeStoreCondDto();
        dto.name = name;
        return dto;
    }

}
