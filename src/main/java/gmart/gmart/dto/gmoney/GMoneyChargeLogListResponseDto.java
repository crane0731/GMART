package gmart.gmart.dto.gmoney;

import gmart.gmart.domain.log.GMoneyChargeLog;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 충전 로그 리스트 응답 DTO
 */
@Getter
@Setter
public class GMoneyChargeLogListResponseDto {

    public Long memberId;

    public Long gmoneyChargeLogId;

    public String chargeType;

    public Long chargeMoney;

    public Long beforeMoney;
    public Long afterMoney;

    public String createdAt;

    /**
     * [생성 메서드]
     * @param gMoneyChargeLog 건머니 충전 로그 엔티티
     * @return GMoneyChargeLogListResponseDto 응답 DTO
     */
    public static GMoneyChargeLogListResponseDto create(GMoneyChargeLog gMoneyChargeLog) {
        GMoneyChargeLogListResponseDto dto = new GMoneyChargeLogListResponseDto();
        dto.memberId = gMoneyChargeLog.getMember().getId();
        dto.gmoneyChargeLogId = gMoneyChargeLog.getId();
        dto.chargeType=gMoneyChargeLog.getChargeType().toString();
        dto.chargeMoney=gMoneyChargeLog.getChargeAmount();
        dto.beforeMoney=gMoneyChargeLog.getBeforeChargeMoney();
        dto.afterMoney=gMoneyChargeLog.getAfterChargeMoney();
        dto.createdAt= DateFormatUtil.DateFormat(gMoneyChargeLog.getCreatedDate());
        return dto;
    }

}
