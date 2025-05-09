package gmart.gmart.dto;

import gmart.gmart.domain.MemberGundamGrade;
import lombok.Getter;
import lombok.Setter;

/**
 * MemberGundamGrade 를 위한 DTO
 */
@Getter
@Setter
public class MemberPreferredGundamGradeDto {

    private String gundamGrade; //건담 등급

    /**
     * 생성 메서드
     */
    public static MemberPreferredGundamGradeDto createDto(MemberGundamGrade gundamGrade) {
        MemberPreferredGundamGradeDto dto = new MemberPreferredGundamGradeDto();
        dto.gundamGrade = gundamGrade.getGundamGrade().toString();
        return dto;
    }

}
