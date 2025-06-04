package gmart.gmart.dto.favorite;

import gmart.gmart.domain.enums.GundamGrade;
import lombok.Getter;
import lombok.Setter;

/**
 * 관심 건담 검색 조건 DTO
 */
@Getter
@Setter
public class SearchFavoriteGundamCondDto {

    private String name;//건담 이름
    private GundamGrade grade; //건담 등급
}
