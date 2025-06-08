package gmart.gmart.dto.favorite;

import lombok.Getter;
import lombok.Setter;

/**
 * 관심 상점 검색 조건 DTO
 */
@Getter
@Setter
public class SearchFavoriteStoreCondDto {

    private String name; //상점 이름

    /**
     * [생성 메서드]
     * @param name 상점 이름
     * @return SearchFavoriteStoreCondDto 검색조건 DTO
     */
    public static SearchFavoriteStoreCondDto create(String name){
        SearchFavoriteStoreCondDto dto = new SearchFavoriteStoreCondDto();
        dto.setName(name);
        return dto;
    }

}
