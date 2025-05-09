package gmart.gmart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 회원 - 건담등급 리스트  DTO
 */
@Getter
@Setter
public class MemberGundamGradeListDto {

    private List<MemberPreferredGundamGradeDto> gundamGrades;


}
