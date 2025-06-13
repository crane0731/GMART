package gmart.gmart.service.order;

import gmart.gmart.repository.order.OrderRepository;
import gmart.gmart.service.item.ItemService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final ItemService itemService;//상품 서비스
    private final MemberService memberService;//회원 서비스

    private final OrderRepository orderRepository; //주문 레파지토리


    // 개발 해야할 비즈니스 로직들

    /**
     * 주문 신청(구매자가 상품 구매 버튼을 누름 -> 상품 구매 신청)
     * 메시지 생성
     */

    /**
     * 주문 수락(판매자가 주문 신청을 승낙함)
     * 주문 거절(판매자가 주문 신청을 거절함)
     * 메시지 생성
     */

    /**
     * 판매자가 상품 배송 상태로 변경(취소도 가능)
     * 메시지 생성
     */

    /**
     * 구매자가 상품을 받으면 구매확정 처리를 함
     * 배송 상태도 배송 완료로 변경
     * 메시지 생성
     */

    /**
     * 구매자가 상품을 받고 환불을 요청함
     * 배송 상태도 배송 완료로 변경
     * 메시지 생성
     */

    /**
     * 판매자가 환불 요청을 승인함
     * 메시지 생성
     */

    /**
     * 구매자가 다시 판매자에게 상품을 보냄
     * 배송 상태 배송으로 변경
     * 메시지 생성
     */

    /**
     * 판매자가 다시 상품을 받고 환불 완료처리를 함
     * 메시지 생성
     */







}
