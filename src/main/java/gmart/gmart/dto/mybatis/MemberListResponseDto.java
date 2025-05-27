package gmart.gmart.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  관리자 회원리스트 조회결과를 응답하기 위한 DTO
 */
@Getter
@Setter
public class MemberListResponseDto {
    private Long id;
    private String loginId;
    private String nickname;
    private Long suspensionCount;
    private Long mannerPoint;
    private Long reportedCount;
    private Long totalSpent;
    private LocalDateTime createdDate;

}
