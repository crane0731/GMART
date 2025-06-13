package gmart.gmart.dto.report;

import gmart.gmart.dto.enums.AdminReportAcceptStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자가 신고에 대해 수락할지, 거절할지 요청하기 위한 DTO
 */
@Getter
@Setter
public class AdminAcceptReportRequestDto {

    private AdminReportAcceptStatus status;
}
