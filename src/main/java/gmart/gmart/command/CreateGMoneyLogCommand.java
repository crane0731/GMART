package gmart.gmart.command;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 건머니 거래 로그 생성 커맨드
 */
@AllArgsConstructor
@Builder
@Getter
public class CreateGMoneyLogCommand {

    private Member member; //회원
    private Order order; //주문
    private GMoneyDeltaType gMoneydeltaType; //건머니 변화 타입
    private String description; //설명
    private Long deltaGMoney; //건머니 변화량
    private Long beforeGMoney; //과거 건머니
    private Long afterGMoney; //현재 건머니
}
