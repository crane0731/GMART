package gmart.gmart.dto.gpoint;

import gmart.gmart.domain.log.GPointLog;
import gmart.gmart.util.DateFormatUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 건포인트 거래 로그 리스트 응답 DTO
 */
@Getter
@Setter
public class GPointLogListResponseDto {

    private Long memberId; //회원 아이디

    private Long gPointLogId; //건머니 거래 로그 아이디

    private Long orderId; //주문 아이디

    private Long itemId; //상품 아이디

    private String gPointDeltaType; //건머니 변화량 타입

    private String description; //설명

    private Long deltaGPoint; //건머니 거래량

    private Long beforeGPoint; //거래 전 건머니

    private Long afterGPoint; //거래 후 건머니

    public String createdAt; //생성일


    /**
     * [생성 메서드]
     * @param gPointLog 건포인트 로그
     * @return GPointLogListResponseDto
     */
    public static GPointLogListResponseDto create(GPointLog gPointLog) {
        GPointLogListResponseDto dto = new GPointLogListResponseDto();
        dto.setMemberId(gPointLog.getMember().getId());
        dto.setGPointLogId(gPointLog.getId());
        dto.setOrderId(gPointLog.getOrder().getId());
        dto.setItemId(gPointLog.getOrder().getItem().getId());
        dto.setGPointDeltaType(gPointLog.getGPointDeltaType().toString());
        dto.setDescription(gPointLog.getDescription());
        dto.setDeltaGPoint(gPointLog.getDeltaGPoint());
        dto.setBeforeGPoint(gPointLog.getBeforeGPoint());
        dto.setAfterGPoint(gPointLog.getAfterGPoint());
        dto.setCreatedAt(DateFormatUtil.DateFormat(gPointLog.getCreatedDate()));
        return dto;

    }
}
