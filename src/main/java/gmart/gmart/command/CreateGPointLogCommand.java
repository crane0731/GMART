package gmart.gmart.command;

import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.enums.GPointDeltaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 건머니 거래 로그 생성 커맨드
 */
@AllArgsConstructor
@Builder
@Getter
public class CreateGPointLogCommand {

    private Member member; //회원
    private Order order; //주문
    private GPointDeltaType gPointDeltaType; //건포인트 변화 타입
    private String description; //설명
    private Long deltaGPoint; //건포인트 변화량
    private Long beforeGPoint; //과거 건포인트
    private Long afterGPoint; //현재 건포인트
}
