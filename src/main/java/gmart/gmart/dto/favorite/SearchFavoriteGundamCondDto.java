package gmart.gmart.dto.favorite;

import gmart.gmart.domain.enums.GundamGrade;
import gmart.gmart.dto.inquiry.SearchInquiryCondDto;
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


    /**
     * [생성 메서드]
     * @param name 건담 이름
     * @param grade 건담 등급
     * @return SearchFavoriteGundamCondDto 검색 조건 DTO
     */
    public static SearchFavoriteGundamCondDto create(String name, GundamGrade grade) {
      SearchFavoriteGundamCondDto dto = new SearchFavoriteGundamCondDto();
      dto.setName(name);
      dto.setGrade(grade);
      return dto;
    }
}
