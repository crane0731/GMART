package gmart.gmart.dto.gmoney;

import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.log.GMoneyLog;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 건머니 로그 리스트 응답 DTO
 */
@Getter
@Setter
public class GMoneyLogListResponseDto {

    private Long memberId; //회원 아이디

    private Long gmoneyLogId; //건머니 거래 로그 아이디

    private Long orderId; //주문 아이디

    private Long itemId; //상품 아이디

    private String gMoneyDeltaType; //건머니 변화량 타입

    private String description; //설명

    private Long deltaGMoney; //건머니 거래량

    private Long beforeGMoney; //거래 전 건머니

    private Long afterGMoney; //거래 후 건머니

    public String createdAt; //생성일

    /**
     * [생성 메서드]
     * @param gmoneyLog 건머니 로그
     * @return GMoneyLogListResponseDto
     */
    public static GMoneyLogListResponseDto create(GMoneyLog gmoneyLog) {
        GMoneyLogListResponseDto dto = new GMoneyLogListResponseDto();
        dto.setGmoneyLogId(gmoneyLog.getId());
        dto.setMemberId(gmoneyLog.getMember().getId());
        dto.setOrderId(gmoneyLog.getOrder().getId());
        dto.setItemId(gmoneyLog.getOrder().getItem().getId());
        dto.setGMoneyDeltaType(gmoneyLog.getGMoneyDeltaType().toString());
        dto.setDescription(gmoneyLog.getDescription());
        dto.setDeltaGMoney(gmoneyLog.getDeltaGMoney());
        dto.setBeforeGMoney(gmoneyLog.getBeforeGMoney());
        dto.setAfterGMoney(gmoneyLog.getAfterGMoney());
        dto.setCreatedAt(DateFormatUtil.DateFormat(gmoneyLog.getCreatedDate()));
        return dto;
    }
}
