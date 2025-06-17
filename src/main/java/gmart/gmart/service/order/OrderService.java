package gmart.gmart.service.order;

import gmart.gmart.domain.Item;
import gmart.gmart.domain.Member;
import gmart.gmart.domain.Order;
import gmart.gmart.domain.enums.AdminMessageType;
import gmart.gmart.domain.enums.GMoneyDeltaType;
import gmart.gmart.domain.enums.GPointDeltaType;
import gmart.gmart.domain.enums.OrderStatus;
import gmart.gmart.dto.adminmessage.CreateAdminMessageRequestDto;
import gmart.gmart.dto.order.CreateOrderRequestDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.OrderCustomException;
import gmart.gmart.repository.order.OrderRepository;
import gmart.gmart.service.admin.AdminMessageService;
import gmart.gmart.service.gmoney.GMoneyLogService;
import gmart.gmart.service.gpoint.GPointLogService;
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
    private final AdminMessageService adminMessageService;//관리자 메시지 서비스
    private final GMoneyLogService gMoneyLogService; //건머니 거래 로그 서비스
    private final GPointLogService gPointLogService; //건포인트 거래 로그 서비스

    private final OrderRepository orderRepository; //주문 레파지토리




    /**
     * [서비스 로직]
     * 주문 신청(구매자가 상품 구매 버튼을 누름 -> 상품 구매 신청)
     * 메시지 생성
     * @param itemId 상품 아이디
     * @param requestDto 요청 DTO
     */
    @Transactional
    public void createOrder(Long itemId, CreateOrderRequestDto requestDto) {

        //현재 로그인한 회원 조회(구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //구매를 원하는 상품 조회
        Item item = itemService.findById(itemId);

        //판매자 조회
        Member seller = item.getStore().getMember();

        //주문 검증
        validateOrder(buyer, seller, item);

        //주문 처리
        processOrder(requestDto, buyer, seller, item);

    }


    /**
     * [서비스 로직]
     * 판매자가 구매자의 구매 신청을 구매확인 처리함(판매자가 구매확인 버튼을 누름)
     * 메시지 생성
     * 건머니 로그 생성
     * 건포이느 로그 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void confirmOrder(Long orderId) {

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //주문확인 처리 로직
        processConfirm(buyer, order, seller);

    }

    /**
     * [서비스 로직]
     * 판매자가 구매 요청을 거절(CANCEL)
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //주문 거절 처리 로직
        processCancel(order, seller, buyer);

    }



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

    /**
     * [생성]
     * @param order 주문 엔티티
     */
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    /**
     * [조회]
     * ID(PK) 값으로 단건 조회
     * @param orderId 주문 아이디
     * @return Order 주문 엔티티
     */
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new OrderCustomException(ErrorMessage.NOT_FOUND_ORDER));
    }

    //==주문 처리 로직==//
    private void processOrder(CreateOrderRequestDto requestDto, Member buyer, Member seller, Item item) {
        //주문 생성
        Order order = Order.create(buyer, seller, item, requestDto.getUsedPoint());

        //주문 저장
        save(order);

        //메시지 생성
        String buyerMessage=seller.getNickname() + " 에게 구매 요청을 보냈습니다.";
        String sellerMessage=buyer.getNickname()+" 님이 구매를 요쳥했습니다.";
        createMessage(buyer,buyerMessage,seller,sellerMessage);
    }

    //==메시지 생성 로직==//
    private void createMessage(Member buyer,String buyerMessage ,Member seller, String sellerMessage) {
        //메시지 생성(구매자)
        adminMessageService.createMessage(buyer.getId(),
                CreateAdminMessageRequestDto.create(buyerMessage, AdminMessageType.TRADE));

        //메시지 생성(판매자)
        adminMessageService.createMessage(seller.getId(),
                CreateAdminMessageRequestDto.create(sellerMessage,AdminMessageType.TRADE));
    }

    //==주문을 위한 검증 로직==//
    private void validateOrder(Member buyer, Member seller, Item item) {
        //판매자는 자신의 아이템을 구매할 수 없음
        validateItemOwner(buyer, seller);

        //이미 대기중인 주문 신청이 있다면 (RESERVED 상태) 재 주문 불가
        validateExistsReservedOrder(buyer, item);
    }

    //==이미 대기중인 주문신청이 있는지 확인하는 로직==//
    private void validateExistsReservedOrder(Member buyer, Item item) {
        Order exists = orderRepository.findByBuyerAndItem(buyer, item, OrderStatus.RESERVED).orElse(null);
        if(exists != null) {
            throw new OrderCustomException(ErrorMessage.ALREADY_RESERVED_ORDER);
        }
    }


    //==구매자의 상품인지 확인 하는 로직 ==//
    private void validateItemOwner(Member buyer, Member seller) {
        if(buyer.getId().equals(seller.getId())) {
            throw new OrderCustomException(ErrorMessage.CANNOT_PURCHASE_OWN_ITEM);
        }
    }

    //==로그 생성 로직==//
    private void createLog(String description,Member buyer, Order order, Long beforeGMoney, Long afterGMoney, Long beforeGPoint, Long afterGPoint) {


        //건머니 로그 생성
        gMoneyLogService.createLog(buyer.getId(), order.getId(),
                GMoneyDeltaType.PURCHASE,description, order.getPaidPrice(), beforeGMoney, afterGMoney);


        //건포인트 로그 생성
        gPointLogService.createLog(buyer.getId(), order.getId(),
                GPointDeltaType.PURCHASE,description, order.getUsedPoint(), beforeGPoint, afterGPoint);
    }

    //==현재 로그인한 회원이 주문의 판매자인지 확인 하는 로직==//
    private void validateOrderSeller(Member seller, Order order) {
        if(!seller.getId().equals(order.getSeller().getId())) {
            throw new OrderCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==주문확인 처리 로직==//
    private void processConfirm(Member buyer, Order order, Member seller) {
        //구매자의 구매 전 건머니,건포인트 금액
        Long beforeGMoney = buyer.getGMoney();
        Long beforeGPoint = buyer.getGPoint();

        //주문 확인 처리 + 구매자의 건머니, 건포인트 차감
        order.confirmOrder();

        //구매자의 구매 후 건머니,건포인트 금액
        Long afterGMoney = buyer.getGMoney();
        Long afterGPoint = buyer.getGPoint();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매 확인 처리를 했습니다.";
        String sellerMessage= buyer.getNickname()+" 에게 구매 확인 메시지를 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //로그 생성
        createLog("상품 구매",buyer, order, beforeGMoney, afterGMoney, beforeGPoint, afterGPoint);
    }

    //==주문 거절 처리 로직==//
    private void processCancel(Order order, Member seller, Member buyer) {

        //구매자의 주문 거절 전 건머니와 건포인트
        Long beforeGMoney = buyer.getGMoney();
        Long beforeGPoint = buyer.getGPoint();

        order.cancelOrder();

        //구매자의 주문 거절 흐 건머니와 건포인트
        Long afterGMoney = buyer.getGMoney();
        Long afterGPoint = buyer.getGPoint();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매 요청을 거절하셨습니다.";
        String sellerMessage= buyer.getNickname()+" 님의 구매 요청을 거절하셨습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //로그 생성
        createLog("주문 취소",buyer, order, beforeGMoney, afterGMoney, beforeGPoint, afterGPoint);
    }






}
