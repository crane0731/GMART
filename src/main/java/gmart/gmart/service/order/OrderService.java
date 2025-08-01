package gmart.gmart.service.order;

import gmart.gmart.domain.*;
import gmart.gmart.domain.enums.*;
import gmart.gmart.dto.RefundOrderRequestDto;
import gmart.gmart.dto.adminmessage.CreateAdminMessageRequestDto;
import gmart.gmart.dto.delivery.TrackingNumberRequestDto;
import gmart.gmart.dto.inquiry.InquiryListResponseDto;
import gmart.gmart.dto.order.BuyerOrderListResponseDto;
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
        Page<Order> pageList = orderRepository.findAllByCondAndSeller(seller, orderStatus, createPageable(page));

        //페이지 객체를 응답 DTO 리스트로 변환
        List<SellerOrderListResponseDto> content = pageList.getContent().stream().map(SellerOrderListResponseDto::create).toList();

        //페이지 응답 DTO 생성 + 반환
        return createPagedSellerResponseDto(content,pageList);
    }

    /**
     * [서비스 로직]
     * 구매자 - 주문 리스트 조회
     * @param orderStatus 주문 상태
     * @param page 페이지 번호
     * @return PagedResponseDto<BuyerOrderListResponseDto> 페이지 응답 리스트 DTO
     */
    public PagedResponseDto<BuyerOrderListResponseDto> findBuyerOrderInformation(OrderStatus orderStatus, int page){

        //현재 로그인한 회원 조회(구매자)
        Member seller = memberService.findBySecurityContextHolder();

        //페이징 주문 리스트 조회
        Page<Order> pageList = orderRepository.findAllByCondAndBuyer(seller, orderStatus, createPageable(page));

        //페이지 객체를 응답 DTO 리스트로 변환
        List<BuyerOrderListResponseDto> content = pageList.getContent().stream().map(BuyerOrderListResponseDto::create).toList();

        //페이지 응답 DTO 생성 + 반환
        return createPagedBuyerResponseDto(content,pageList);

    }


    /**
     * [서비스 로직]
     * 구매 신청(구매자가 상품 구매 버튼을 누름 -> 상품 구매 신청)
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
     * 구매자가 구매 요청을 취소함
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void cancelOrder(Long orderId){

        //현재 로그인한 회원 조회(구매자)
        Member buyer = memberService.findBySecurityContextHolder();

        //구매취소를 원하는 상품 조회
        Order order = findById(orderId);

        //판매자 조회
        Member seller = order.getSeller();

        //현재 로그인한 회원과 주문의 구매자가 같은 회원이아닌지 검증
        validateOrderBuyer(order, buyer);

        //구매 취소 로직
        processOrderCancel(order, seller, buyer);

    }

    /**
     * [서비스 로직]
     * 판매자가 구매자의 구매 요청 거절
     * 메시지 생성
     * @param orderId 주문 아이디
     */
    @Transactional
    public void rejectOrder(Long orderId){

        //현재 로그인한 회원 조회(판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //구매취소를 원하는 상품 조회
        Order order = findById(orderId);

        //판매자 조회
        Member buyer = order.getBuyer();

        //판매자가 해당 주문의 판매자인지 확인
        validateOrderSeller(seller,order);

        //주문 거절 처리 로직
        processOrderReject(order, seller, buyer);


    }


    //구매 신청 수락
    @Transactional
    public void acceptOrder(Long orderId){

        //현재 로그인한 회원 (판매자)
        Member seller = memberService.findBySecurityContextHolder();

        //주문 조회
        Order order = findById(orderId);

        //구매자 조회
        Member buyer = order.getBuyer();

        //현재 로그인한 회원이 주문의 판매자 인지 확인
        validateOrderSeller(seller,order);

        //주문 수락 처리 로직
        processOrderAccept(buyer, order, seller);

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

        //이미 판매완료된 상품이라면 주문 불가
        validateSoldOut(item);
    }

    //==이미 판매완료된 상품인지 확인 하는 로직==//
    private void validateSoldOut(Item item) {
        if(item.getSaleStatus().equals(SaleStatus.SOLD_OUT)){
            throw new OrderCustomException(ErrorMessage.SOLD_OUT);
        }
    }


    //==구매자의 상품인지 확인 하는 로직 ==//
    private void validateItemOwner(Member buyer, Member seller) {

    }

    //==로그 생성 로직==//
    private void createLog(String description,Member buyer, Order order, Long beforeGMoney, Long afterGMoney, Long beforeGPoint, Long afterGPoint) {


        //건머니 로그 생성
        gMoneyLogService.createLog(buyer, order,
                GMoneyDeltaType.PURCHASE,description, order.getPaidPrice(), beforeGMoney, afterGMoney);

        //만약 건포인트에 변화가 있다면
        if(!beforeGPoint.equals(afterGPoint)){
            //건포인트 로그 생성
            gPointLogService.createLog(buyer, order,
                    GPointDeltaType.PURCHASE,description, order.getUsedPoint(), beforeGPoint, afterGPoint);
        }

    }

    //==현재 로그인한 회원이 주문의 판매자인지 확인 하는 로직==//
    private void validateOrderSeller(Member seller, Order order) {
        if(!seller.getId().equals(order.getSeller().getId())) {
            throw new OrderCustomException(ErrorMessage.NO_PERMISSION);
        }
    }


    //==페이징 생성 메서드==//
    private Pageable createPageable(int page) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 0, 10개씩 보여줌
        return pageable;
    }

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<SellerOrderListResponseDto> createPagedSellerResponseDto(List<SellerOrderListResponseDto> content, Page<Order> page) {
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

    //==페이징 응답 DTO 생성==//
    private PagedResponseDto<BuyerOrderListResponseDto> createPagedBuyerResponseDto(List<BuyerOrderListResponseDto> content, Page<Order> page) {
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

    //==구매 취소 로직==//
    private void processOrderCancel(Order order, Member seller, Member buyer) {
        //주문 취소 처리
        order.cancelOrder();

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 에게 구매 취소 알림을 보냈습니다.";
        String sellerMessage= buyer.getNickname()+" 님이 구매를 취소하였습니다..";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==현재 로그인한 회원과 주문의 구매자가 같은 회원이아닌지 검증하는 메소드==//
    private void validateOrderBuyer(Order order, Member buyer) {
        //현재 로그인한 회원과 주문의 구매자가 같은 회원이아닌지 검증
        if(!order.getBuyer().equals(buyer)){
            throw new OrderCustomException(ErrorMessage.NO_PERMISSION);
        }
    }

    //==주문 거절 처리 로직==//
    private void processOrderReject(Order order, Member seller, Member buyer) {
        order.rejectOrder();


        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매요청을 거절 하엿습니다.";
        String sellerMessage= buyer.getNickname()+" 님에게 구매 요청 거절 알림을 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);
    }

    //==주문 수락 처리 로직==//
    private void processOrderAccept(Member buyer, Order order, Member seller) {
        //주문 수락 전의 구매자의 건머니와 건포인트
        Long beforeGMoney = buyer.getGMoney();
        Long beforeGPoint = buyer.getGPoint();

        //주문 수락 처리
        order.acceptOrder();

        //배송 정보 생성 + 저장
        createDelivery(buyer, order);

        //메시지 생성
        String buyerMessage= seller.getNickname() + " 님이 구매요청을 수락 하였습니다.";
        String sellerMessage= buyer.getNickname()+" 님에게 구매 요청 수락 알림을 보냈습니다.";
        createMessage(buyer,buyerMessage, seller,sellerMessage);

        //주문 수락 후의 구매자의 건머니와 건포인트
        Long afterGMoney = buyer.getGMoney();
        Long afterGPoint = buyer.getGPoint();

        //구매자의 건머니 , 건포인트 로그 생성
        String logDescription= "상품 구매";
        createLog(logDescription, buyer, order,beforeGMoney,afterGMoney,beforeGPoint,afterGPoint);
    }


    //==배송 정보 생성 + 저장==//
    private void createDelivery(Member buyer, Order order) {
        Delivery delivery = Delivery.create(buyer);
        order.setDelivery(delivery);
        deliveryService.save(delivery);
    }

}
