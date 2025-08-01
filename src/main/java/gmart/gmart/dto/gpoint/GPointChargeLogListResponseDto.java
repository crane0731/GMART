package gmart.gmart.dto.gpoint;

import gmart.gmart.domain.log.GPointChargeLog;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 충전 로그 리스트 응답 DTO
 */
@Getter
@Setter
public class GPointChargeLogListResponseDto {

    public Long memberId;

    public Long gpointChargeLogId;

    public String chargeType;

    public Long chargePoint;

    public Long beforePoint;
    public Long afterPoint;

    public String createdAt;

    /**
     * [생성 메서드]
     * @param gPointChargeLog 건포인트 충전 로그 엔티티
     * @return GPointChargeLogListResponseDto 응답 DTO
     */
    public static GPointChargeLogListResponseDto create(GPointChargeLog gPointChargeLog){

        GPointChargeLogListResponseDto dto = new GPointChargeLogListResponseDto();
        dto.memberId = gPointChargeLog.getMember().getId();
        dto.gpointChargeLogId = gPointChargeLog.getId();
        dto.chargeType = gPointChargeLog.getChargeType().toString();
        dto.chargePoint = gPointChargeLog.getChargeAmount();
        dto.beforePoint = gPointChargeLog.getBeforeChargePoint();
        dto.afterPoint = gPointChargeLog.getAfterChargePoint();
        dto.createdAt = DateFormatUtil.DateFormat(gPointChargeLog.getCreatedDate());
        return dto;

    }
}
