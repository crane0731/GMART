package gmart.gmart.dto.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 계정 일시 정지 요청 DTO
 */
@Getter
@Setter
public class MemberSuspensionRequestDto {
    private String reason; //정지 사유
    private Long day; //정지 기간

}
