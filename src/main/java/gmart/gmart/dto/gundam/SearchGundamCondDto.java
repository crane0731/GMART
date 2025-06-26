package gmart.gmart.dto.gundam;

import gmart.gmart.domain.enums.GundamGrade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.MergedAnnotations;

/**
 * 건담 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGundamCondDto {

    private String name; //이름

    private GundamGrade grade; //등급

    /**
     * [생성 메서드]
     * @param name 이름
     * @param grade 등급
     * @return SearchGundamCondDto 응답 DTO
     */
    public static SearchGundamCondDto create(String name, GundamGrade grade) {
        SearchGundamCondDto dto = new SearchGundamCondDto();
        dto.setName(name);
        dto.setGrade(grade);
        return dto;
    }
}
