package gmart.gmart.dto.gundam;

import gmart.gmart.domain.enums.GundamGrade;
import lombok.Getter;
import lombok.Setter;

/**
 * 건담 검색 조건 DTO
 */
@Getter
@Setter
public class SearchGundamCondDto {

    private String name; //이름

    private GundamGrade grade; //등급
}
