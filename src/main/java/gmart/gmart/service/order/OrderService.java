package gmart.gmart.service.order;

import gmart.gmart.domain.*;
import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.RefundOrderRequestDto;
import gmart.gmart.dto.adminmessage.CreateAdminMessageRequestDto;
import gmart.gmart.dto.delivery.TrackingNumberRequestDto;
import gmart.gmart.dto.inquiry.InquiryListResponseDto;
import gmart.gmart.dto.order.CancelOrderRequestDto;
import gmart.gmart.dto.order.CreateOrderRequestDto;
import gmart.gmart.dto.order.SellerOrderListResponseDto;
import gmart.gmart.dto.page.PagedResponseDto;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.OrderCustomException;
import gmart.gmart.repository.order.OrderRepository;
import gmart.gmart.service.admin.AdminMessageService;
import gmart.gmart.service.delivery.DeliveryService;
import gmart.gmart.service.gmoney.GMoneyLogService;
import gmart.gmart.service.gpoint.GPointLogService;
import gmart.gmart.service.item.ItemService;
import gmart.gmart.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final DeliveryService deliveryService;//배송 서비스

    private final OrderRepository orderRepository; //주문 레파지토리


    /**
     * [서비스 로직]
     * 판매자 - 주문 리스트 조회
     * @param orderStatus 주문 상태
     * @param page 페이지 번호
     * @return PagedResponseDto<SellerOrderListResponseDto> 페이지 응답 리스트 DTO
     */
    public PagedResponseDto<SellerOrderListResponseDto> findSellerOrderInformation(OrderStatus orderStatus, int page){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //페이징 주문 리스트 조회
        Page<Order> pageList = orderRepository.findAllByCond(seller, orderStatus, createPageable(page));

        //페이지 객체를 응답 DTO 리스트로 변환
        List<SellerOrderListResponseDto> content = pageList.getContent().stream().map(SellerOrderListResponseDto::create).toList();

        //페이지 응답 DTO 생성 + 반환
        return createPagedResponseDto(content,pageList);
    }

    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<SellerOrderListResponseDto> createPagedResponseDto(List<SellerOrderListResponseDto> content, Page<Order> page) {
        return new PagedResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }


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
     * 구매자가 주문 취소함
     * 아직 판매자가 주문을 확인하기 전 상태에서만 가능 (주문 예약 상태)
     * @param orderId 주문 아이디
     * @param requestDto 주문 취소 요청 DTO
     */
    @Transactional
    public void cancelOrderByBuyer(Long orderId , CancelOrderRequestDto requestDto) {

        //현재 로그인한 회원 조회 (구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //판매자는 자신의 아이템을 주문취소 요청 할 수 없음
        validateItemOwner(buyer, seller);

        //판매자 주문 취소 로직
        processOrderCancelByBuyer(order, seller, buyer,requestDto);
    }

    /**
     * [서비스 로직]
     * 구매자가 판매자에게 주문 취소를 요청함
     * 주문 상태가 아직 주문확인 상태 즉,판매자가 주문 확인을 하고 아직 상품을 배송하기 전인 상태 만 가능
     * @param orderId 주문 아이디
     */
    @Transactional
    public void cancelRequestByBuyer(Long orderId,CancelOrderRequestDto requestDto) {

        //현재 로그인한 회원 조회 (구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //판매자는 자신의 아이템을 주문취소 요청 할 수 없음
        validateItemOwner(buyer, seller);

        //구매자 주문 취소 요청 로직
        processCancelRequestByBuyer(order, seller, buyer,requestDto);

    }

    /**
     * [서비스 로직]
     * 판매자가 구매자의 구매 신청을 구매확인 처리함(판매자가 구매확인 버튼을 누름)
     * 메시지 생성
     * 건머니 로그 생성
     * 건포인트 로그 생성
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
     * 판매자가 구매자의 구매 요청을 거절함
     * 처음 구매자가 주문 신청을 넣으면 거절가능
     * @param orderId 주문 아이디
     * @param requestDto 주문 취소 요청 DTO
     */
    @Transactional
    public void rejectOrderRequestBySeller(Long orderId,CancelOrderRequestDto requestDto) {

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //주문 요청 거절 로직
        processRejectOrderRequestBySeller(requestDto, order, seller, buyer);

    }

    /**
     * [서비스 로직]
     * 판매자가 주문을 취소함
     * 상품을 배송하기 전까지 가능
     * @param orderId 주문 아이디
     * @param requestDto 주문 취소 요청 DTO
     */
    @Transactional
    public void cancelOrder(Long orderId,CancelOrderRequestDto requestDto){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //주문 취소 로직
        processCancel(order, seller, buyer,requestDto);

    }

    /**
     * [서비스 로직]
     * 판매자가 구매자의 주문 취소 요청을 승인
     * 메시지 생성
     * 건머니 로그 생성
     * 건포인트 로그 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void acceptCancelOrderRequestBySeller(Long orderId){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //취소 요청 승인 + 주문 취소 처리
        processAcceptCancelOrderRequestBySeller(buyer, order, seller);
    }


    /**
     * [서비스 로직]
     * 판매자가 구매자의 주문 취소 요청을 거절
     * 즉, 주문 절차가 원래대로 진행됨
     * 주문 상태 CANCEL_REQUESTED -> CONFIRM
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void rejectCancelOrderRequestBySeller(Long orderId){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //판매자가 구매자의 주문 취소 요청 거절 로직
        processRejectCancelOrderRequestBySeller(order, seller, buyer);


    }


    /**
     * [서비스 로직]
     * 판매자가 배송 준비 상태로 설정하는 기능
     * 배송 생성
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void readyDelivery(Long orderId){

        //현재 로그인한 회원(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //배송 생성 로직
        processReadyDelivery(buyer, seller, order);
    }

    /**
     * [서비스 로직]
     * 판매자가 상품 배송을 시작함을 알리는 서비스
     * 메시지 생성
     * @param orderId 주문 아이디
     * @param requestDto 요청 DTO
     */
    @Transactional
    public void shipItem(Long orderId, TrackingNumberRequestDto requestDto){

        //현재 로그인한 회원(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //상품 배송 시작 처리
        processShipItem(requestDto, order, seller, buyer);
    }

    /**
     * [서비스 로직]
     * 판매자가 상품 배송 준비를 취소
     * 배송 상태를 CANCELED 로 변경
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void cancelReadyDelivery(Long orderId){

        //현재 로그인한 회원(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //현재 로그인한 회원이 주문의 판매자인지 확인
        validateOrderSeller(seller, order);

        //구매자 조회
        Member buyer = order.getBuyer();

        //배송 준비 취소 처리 로직
        processCancelReadyDelivery(order, seller, buyer);

    }

    /**
     * [서비스 로직]
     * 구매자가 상품을 받으면 구매확정 처리를 함
     * 배송 상태도 배송 완료로 변경
     * 판매자에게 판매 대금 입금
     * 메시지 생성
     * 건머니 로그 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void completeOrder(Long orderId){

        //현재 로그인한 회원(구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //구매확정 처리 로직
        processCompletedOrder(seller, order, buyer);

    }


    /**
     * [서비스 로직]
     * 구매자가 상품 수령 후 환불을 요청함
     * 배송 상태는 '배송 완료'로 변경됨
     * 메시지 생성
     * @param orderId 주문 아이디
     * @param requestDto 환불 요청 DTO
     */
    @Transactional
    public void refundRequest(Long orderId, RefundOrderRequestDto requestDto){

        //현재 로그인한 회원 조회 (구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        // 주문 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //주문 반품 요청 로직
        processRefundRequest(requestDto, order, seller, buyer);

    }

    /**
     * [서비스 로직]
     * 판매자가 환불 요청을 거절함
     * 환불 요청을 거절했다는 것은 거래가 정상적으로 이루어졌다는 뜻 -> 구매 확정 처리
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void rejectRefundRequest(Long orderId){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문조회
        Order order = findById(orderId);

        //구매자 조회
        Member buyer = order.getBuyer();

        //구매 확정 처리 로직
        processCompletedOrder(seller, order, buyer);
    }

    /**
     * [서비스 로직]
     * 판매자가 환불 요청을 승인함
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void acceptRefundRequest(Long orderId){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문조회
        Order order = findById(orderId);

        //구매자 조회
        Member buyer = order.getBuyer();

        //판매자 환불 요청 승인 처리
        processAcceptRefundRequest(order, seller, buyer);
    }

    /**
     * [서비스 로직]
     * 구매자가 다시 판매자에게 상품을 보냄
     * 구매자는 환불 송장 입력
     * 배송 상태 배송으로 변경
     * 메시지 생성
     * @param orderId 주문 아이디
     * @param requestDto 송장 번호 요청 DTO
     */
    @Transactional
    public void refundShipItem(Long orderId, TrackingNumberRequestDto requestDto){

        //현재 로그인한 회원 (구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //환불 배송 처리 로직
        processRefundShipItem(requestDto, order, seller, buyer);

    }

    /**
     * [서비스 로직]
     * 판매자가 다시 상품을 받고 환불 완료처리를 함
     * 판매자는 부담했던 배송비를 다시 받음
     * 구매자는 판매자의 배송비를 제외한 나머지 금액을 환불 받음
     * 배송비는 포인트에서 차감되지않고 결제 금액에서 차감됨
     * 메시지 생성
     * 건머니 , 건머니 로그 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void refundComplete(Long orderId){
        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //구매자 조회
        Member buyer = order.getBuyer();

        //환불 완료 처리
        processRefundComplete(seller, buyer, order);

    }

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
        String sellerMessage=buyer.getNickname()+" 님이 구매를 요청했습니다.";
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

        //이미 대기중인 주문 신청이 있다면 (RESERVED 또는 CANCEL_REQUESTED 상태) 재 주문 불가
        validateExistsReservedOrder(buyer, item);

    }

    //==이미 대기중인 주문신청이 있는지 확인하는 로직==//
    private void validateExistsReservedOrder(Member buyer, Item item) {
        Order exists = orderRepository.findByBuyerAndItem(buyer, item, OrderStatus.RESERVED,OrderStatus.CANCEL_REQUESTED).orElse(null);
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

    //==주문 취소 로직==//
    private void processCancel(Order order, Member seller, Member buyer,CancelOrderRequestDto requestDto) {

        //구매자의 주문 거절 전 건머니와 건포인트
        Long beforeGMoney = buyer.getGMoney();
        Long beforeGPoint = buyer.getGPoint();

        order.cancelOrderBySeller(requestDto.getCancelReason());

        //구매자의 주문 거절 후 건머니와 건포인트
        Long afterGMoney = buyer.getGMoney();
        Long afterGPoint = buyer.getGPoint();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매 요청을 거절하셨습니다. 사유 : " + order.getCancelReason();
        String sellerMessage= buyer.getNickname()+" 님의 구매 요청을 거절하셨습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //로그 생성
        createLog("주문 취소",buyer, order, beforeGMoney, afterGMoney, beforeGPoint, afterGPoint);
    }

    //==구매자 구매 취소 요청 로직==//
    private void processCancelRequestByBuyer(Order order, Member seller, Member buyer,CancelOrderRequestDto requestDto) {
        //구매 취소 요청  로직
        order.cancelRequestByBuyer(requestDto.getCancelReason());

        String buyerMessage= seller.getNickname() + " 님에게 구매 취소를 요쳥했습니다.";
        String sellerMessage= buyer.getNickname()+" 님이 구매 취소 요청을 보냈습니다. 사유 " + order.getCancelReason();
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==판매자 주문 취소 로직==//
    private void processOrderCancelByBuyer(Order order, Member seller, Member buyer,CancelOrderRequestDto requestDto) {
        //주문 취소
        order.cancelOrderByBuyer(requestDto.getCancelReason());

        String buyerMessage= seller.getNickname() + " 님의 상품 구매를 취소했습니다.";
        String sellerMessage= buyer.getNickname()+" 님이 구매를 취소했습니다. 사유 :"+order.getCancelReason();
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==취소 요청 승인 + 주문 취소 처리 로직 ==//
    private void processAcceptCancelOrderRequestBySeller(Member buyer, Order order, Member seller) {
        //구매자의 주문 거절 전 건머니와 건포인트
        Long beforeGMoney = buyer.getGMoney();
        Long beforeGPoint = buyer.getGPoint();

        //취소 요청 승인 + 주문 취소 처리
        order.acceptCancelRequestBySeller();

        //구매자의 주문 거절 흐 건머니와 건포인트
        Long afterGMoney = buyer.getGMoney();
        Long afterGPoint = buyer.getGPoint();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 주문 취소 요청을 승인하였습니다.";
        String sellerMessage= buyer.getNickname()+" 님의 주문 취소 요청을 승인하였습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //로그 생성
        createLog("주문 취소", buyer, order, beforeGMoney, afterGMoney, beforeGPoint, afterGPoint);
    }

    //==주문 요청 거절 로직==//
    private void processRejectOrderRequestBySeller(CancelOrderRequestDto requestDto, Order order, Member seller, Member buyer) {
        order.rejectOrderRequestBySeller(requestDto.getCancelReason());

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매 요청을 거절하셨습니다. 사유 : " + order.getCancelReason();
        String sellerMessage= buyer.getNickname()+" 님의 구매 요청을 거절하셨습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==판매자가 구매자의 주문 취소 요청 거절 로직==//
    private void processRejectCancelOrderRequestBySeller(Order order, Member seller, Member buyer) {
        order.rejectCancelRequestBySeller();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 주문 취소 요청을 거절하였습니다. 계속해서 거래가 진행됩니다.";
        String sellerMessage= buyer.getNickname()+" 님의 주문 취소 요청을 거절하였습니다. 계속해서 거래가 진행됩니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==배송 생성 로직==//
    private void processReadyDelivery(Member buyer, Member seller, Order order) {

        //만약 배송 정보가 있다면
        if(order.getDelivery()!=null){
            //기존 배송 을 다시 준비 완료 상태로 되돌림
            order.getDelivery().ready();
        }else{
            //배송 생성
            Delivery delivery = Delivery.create(seller,buyer, RefundStatus.NOT_REFUND);

            //주문에 배송 설정
            order.setDelivery(delivery);

            //배송 저장
            deliveryService.save(delivery);
        }
        //메시지 생성
        String buyerMessage= " 배송 준비! "+ seller.getNickname() + " 님이 상품 발송 준비를 완료했습니다.";
        String sellerMessage= " 배송 준비! "+ buyer.getNickname()+" 님에게 상품 발송 준비를 알렸습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==상품 배송 시작 처리 하는 로직==//
    private void processShipItem(TrackingNumberRequestDto requestDto, Order order, Member seller, Member buyer) {
        //배송 시작 처리
        order.shipItem(requestDto.getTrackingNumber());

        //메시지 생성
        String buyerMessage= " 배송 시작! "+ seller.getNickname() + " 님이 상품을 발송하였습니다.";
        String sellerMessage= " 배송 시작! "+ buyer.getNickname()+" 님에게 상품을 발송하였습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==배송 준비 취소 처리 로직==//
    private void processCancelReadyDelivery(Order order, Member seller, Member buyer) {
        //배송 준비 취소 처리
        order.cancelReadyDelivery();

        //메시지 생성
        String buyerMessage= " 배송 준비 취소 "+ seller.getNickname() + " 님이 상품 발송 준비를 취소했습니다.";
        String sellerMessage= " 배송 준비 취소"+ buyer.getNickname()+" 님에게 상품 발송 준비 취소를 알렸습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }
    //==구매확정 처리 로직==//
    private void processCompletedOrder(Member seller, Order order, Member buyer) {
        //구매 확정 전 판매자의 건머니
        Long beforeGMoney = seller.getGMoney();

        //구매 확정 처리
        order.completedOrder();

        //구매 확정 후 판매자의 건머니
        Long afterGMoney = seller.getGMoney();

        //메시지 생성
        String buyerMessage= " 구매 확정 완료! "+ seller.getNickname() + " 님에게 구매 확정 알림을 보냈습니다.";
        String sellerMessage= " 구매 확정 완료!"+ buyer.getNickname()+" 님이 구매 확정을 완료했습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //건머니 로그 생성

        String description = "구매 확정 상품 대금 수령";

        gMoneyLogService.createLog(seller.getId(), order.getId(),
                GMoneyDeltaType.SALE,description, order.getPaidPrice(), beforeGMoney, afterGMoney);
    }

    //==주문 반품 요청 로직==//
    private void processRefundRequest(RefundOrderRequestDto requestDto, Order order, Member seller, Member buyer) {
        order.refundRequest(requestDto.getRefundReason());

        //메시지 생성
        String buyerMessage=  seller.getNickname() + " 님에게 환불 요청을 보냈습니다.";
        String sellerMessage= buyer.getNickname()+" 님이 환불 요청을 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==판매자 환불 요청 승인 처리 로직==//
    private void processAcceptRefundRequest(Order order, Member seller, Member buyer) {
        //판매자 환불 요청 승인 처리
        order.acceptRefundRequest();

        //메시지 생성
        String buyerMessage=  seller.getNickname() + " 님이 환불 요청을 승인하였습니다.";
        String sellerMessage= buyer.getNickname()+" 님에게 환불 요청 승인 알림을 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==환불 배송 처리 로직==//
    private void processRefundShipItem(TrackingNumberRequestDto requestDto, Order order, Member seller, Member buyer) {
        //주문 환불 배송 처리
        order.refundShipItem(requestDto.getTrackingNumber());

        //메시지 생성
        String buyerMessage= " 환불 배송 시작! "+ seller.getNickname() + " 님에게 상품을 발송하였습니다.";
        String sellerMessage= " 환불 배송 시작! "+ buyer.getNickname()+" 님이 상품을 발송하였습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==환불 완료 관련 로그 생성 로직==//
    private void createLogByRefundComplete(Member seller, Order order, Long beforeSellerGMoney, Long afterSellerGMoney, Member buyer, Long beforeBuyerGMoney, Long afterBuyerGMoney, Long beforeBuyerGPoint, Long afterBuyerGPoint) {
        gMoneyLogService.createLog(seller.getId(), order.getId(),GMoneyDeltaType.REFUND,"환불 완료, 배송비 입금", order.getDeliveryPrice(), beforeSellerGMoney, afterSellerGMoney);

        gMoneyLogService.createLog(buyer.getId(), order.getId(),GMoneyDeltaType.REFUND,"환불 완료, 결제 대금 환불 (배송비 제외)", order.getPaidPrice()- order.getDeliveryPrice(), beforeBuyerGMoney, afterBuyerGMoney);

        gPointLogService.createLog(buyer.getId(), order.getId(),GPointDeltaType.REFUND,"환불 완료, 결제 포인트 환불", order.getUsedPoint(), beforeBuyerGPoint, afterBuyerGPoint);
    }

    //==환불 완료 처리 로직==//
    private void processRefundComplete(Member seller, Member buyer, Order order) {
        //판매자 이전 건머니
        Long beforeSellerGMoney = seller.getGMoney();

        //구매자 이전 건머니 , 건포인트
        Long beforeBuyerGMoney = buyer.getGMoney();
        Long beforeBuyerGPoint = buyer.getGPoint();

        //환불 완료 처리
        order.refundComplete();

        //판매자 이후 건머니
        Long afterSellerGMoney = seller.getGMoney();

        //구매자 이후 건머니 , 건포인트
        Long afterBuyerGMoney = buyer.getGMoney();
        Long afterBuyerGPoint = buyer.getGPoint();

        //메시지 생성
        String buyerMessage= " 환불 완료! "+ seller.getNickname() + " 님이 환불 처리를 완료하였습니다.";
        String sellerMessage= " 환불 완료! "+ buyer.getNickname()+" 님에게 환불 처리 완료 알림을 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //로그 생성
        createLogByRefundComplete(seller, order, beforeSellerGMoney, afterSellerGMoney, buyer, beforeBuyerGMoney, afterBuyerGMoney, beforeBuyerGPoint, afterBuyerGPoint);
    }
}
