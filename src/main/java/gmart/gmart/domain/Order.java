package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.*;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.OrderCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 주문 도메인
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("주문 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @org.hibernate.annotations.Comment("구매자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @org.hibernate.annotations.Comment("판매자 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @org.hibernate.annotations.Comment("상품 아이디")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @org.hibernate.annotations.Comment("배송 아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @org.hibernate.annotations.Comment("주문 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @org.hibernate.annotations.Comment("총 가격")
    @Column(name = "total_price")
    private Long totalPrice;

    @org.hibernate.annotations.Comment("결제할 가격")
    @Column(name = "paid_price")
    private Long paidPrice;

    @org.hibernate.annotations.Comment("상품 가격")
    @Column(name = "item_price")
    private Long itemPrice;

    @org.hibernate.annotations.Comment("배송비")
    @Column(name = "delivery_price")
    private Long deliveryPrice;

    @org.hibernate.annotations.Comment("사용 포인트")
    @Column(name = "used_point")
    private Long usedPoint;

    @org.hibernate.annotations.Comment("취소,환불 사유")
    @Column(name = "cancel_reason")
    private String cancelReason;

    @Comment("취소/환불 사유 작성자 역할 (BUYER / SELLER)")
    @Column(name = "cancel_reason_writer")
    @Enumerated(EnumType.STRING)
    private OrderRole cancelReasonWriter;

    @org.hibernate.annotations.Comment("취소,환불 요청 일자")
    @Column(name = "cancel_requested_date")
    private LocalDateTime cancelRequestedDate;

    @org.hibernate.annotations.Comment("취소,환불 처리 완료 일자")
    @Column(name = "cancel_completed_date")
    private LocalDateTime cancelCompletedDate;

    @org.hibernate.annotations.Comment("에스크로 상태")
    @Column(name = "escrow_status")
    @Enumerated(EnumType.STRING)
    private EscrowStatus escrowStatus;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    @OneToOne(mappedBy = "order")
    private Review review;


    /**
     * [생성 메서드]
     * @param buyer 구매자 엔티티
     * @param seller 판매자 엔티티
     * @param item 상품 엔티티
     * @param usedPoint 사용 포인트
     * @return Order 주문 엔티티
     */
    public static Order create(Member buyer , Member seller , Item item, Long usedPoint){
        Order order = new Order();

        order.setBuyer(buyer);
        order.setSeller(seller);

        order.item = item;

        order.orderStatus=OrderStatus.RESERVED;

        order.itemPrice = item.getItemPrice();
        order.deliveryPrice = item.getDeliveryPrice();

        if(usedPoint>order.buyer.getGPoint()){
            throw new OrderCustomException(ErrorMessage.NOT_ENOUGH_GPOINT);
        }

        order.usedPoint = usedPoint;

        Long total = order.setTotalPrice(item.getItemPrice(), item.getDeliveryPrice());
        Long paid = order.setPaidPrice(total, usedPoint);

        if (paid>order.buyer.getGMoney()){
            throw new OrderCustomException(ErrorMessage.NOT_ENOUGH_GMONEY);
        }

        order.escrowStatus= EscrowStatus.NONE;
        order.deleteStatus = DeleteStatus.UNDELETED;

        return order;
    }

    /**
     * [연관관계 편의 메서드]
     * Order<->Delivery
     * @param delivery 배송 엔티티
     */
    public void setDelivery(Delivery delivery){

        //배송 : 주문 상태가 주문확인 인지 검증
        validateConfirmedByDelivery();

        this.delivery=delivery;
        delivery.setOrder(this);

    }

    /**
     * [비즈니스 로직]
     * 주문 상태를 배송 시작함으로 변경
     */
    public void shipItem(String trackingNumber){

        this.delivery.setTrackingNumber(trackingNumber);
        this.orderStatus=OrderStatus.SHIPPED;
    }

    /**
     * [비즈니스 로직]
     * 환불용 배송을 설정함
     * 환불 송장번호 세팅
     * @param refundTrackingNumber
     */
    public void refundShipItem(String refundTrackingNumber){
        this.delivery.setRefundTrackingNumber(refundTrackingNumber);
        this.orderStatus=OrderStatus.REFUND_SHIPPED;
    }


    /**
     * [총 가격 계산]
     * @param itemPrice 상품 가격
     * @param deliveryPrice 배송비
     * @return Long 총 가격
     */
    public Long setTotalPrice(Long itemPrice, Long deliveryPrice){
        this.totalPrice= itemPrice+deliveryPrice;
        return this.totalPrice;
    }


    /**
     * [결제 가격 계산]
     * @param totalPrice 총 가격
     * @param usedPoint 사용 포인트
     * @return Long 결제 가격
     */
    public Long setPaidPrice(Long totalPrice, Long usedPoint){
        if(usedPoint<1000){
            throw new OrderCustomException(ErrorMessage.POINT_MINIMUM_REQUIRED);
        } else if (usedPoint>totalPrice) {
            throw new OrderCustomException(ErrorMessage.POINT_EXCEEDS_TOTAL);
        } else {
            this.paidPrice= totalPrice-usedPoint;
            return this.paidPrice;

        }
    }


    /**
     * [연관관계 편의 메서드]
     * @param buyer 구매자 엔티티
     */
    public void setBuyer(Member buyer) {
        this.buyer = buyer;
        buyer.getBuyOrders().add(this);
    }

    /**
     * [연관관계 편의 메서드]
     * @param seller 판매자 엔티티
     */
    public void setSeller(Member seller) {
        this.seller = seller;
        seller.getSaleOrders().add(this);
    }

    /**
     * [비즈니스 로직]
     * 판매자가 주문 수락 처리
     */
    public void confirmOrder(){
        //검증 로직
        validateReservedByConfirm();
        
        //구매자의 결제 금액 처리
        deductBuyerPayment();

        //구매 확인 상태 처리
        markConfirmed();
    }

    /**
     * [비즈니스 로직]
     * 판매자가 구매자의 주문 취소 요청을 수락
     */
    public void acceptCancelRequestBySeller(){
        //검증 로직
        validateCancelRequestByCancel();

        //구매자의 결제 데이터 복구
        recoveryBuyerPayment();

        //구매 취소 상태 처리
        markCanceled();

        //주문 캔슬
        this.orderStatus = OrderStatus.CANCELLED;

        this.cancelCompletedDate = LocalDateTime.now();

    }

    /**
     * [비즈니스 로직]
     * 판매자가 구매자의 주문 취소 요청을 거절
     */
    public void rejectCancelRequestBySeller(){

        //주문 상태가 주문 취소 요청 인지 검증
        validateCancelRequestByCancel();

        this.orderStatus = OrderStatus.CONFIRMED;
    }


    /**
     * [비즈니스 로직]
     * 판매자가 구매자의 구매 요청을 거절함
     */
    public void rejectOrderRequestBySeller(String cancelReason){

        //주문의 상태가 이미 예약 상태인지 확인
        //RESERVED 상태가 아니라면 구매 거절 불가
        validateReservedByCancel();

        //주문 캔슬
        this.orderStatus=OrderStatus.REJECTED;

        this.cancelReason = cancelReason;

        this.cancelReasonWriter=OrderRole.SELLER;

        this.cancelRequestedDate = LocalDateTime.now();

        this.cancelCompletedDate = LocalDateTime.now();
    }



    /**
     * [비즈니스 로직]
     * 판매자가 주문 거절 처리
     */
    public void cancelOrderBySeller(String cancelReason){

        //주문의 상태가 주문 확인 상태인지 검증
        validateConfirmedByCancel();

        //만약 주문 상태가 주문 확인 처리 상태였다면 다시 복구

        //구매자의 결제 데이터 복구
        recoveryBuyerPayment();

        //구매 취소 상태 처리
         markCanceled();

        //주문 캔슬
        this.orderStatus=OrderStatus.CANCELLED;

        this.cancelReason = cancelReason;

        this.cancelReasonWriter=OrderRole.SELLER;

        this.cancelRequestedDate = LocalDateTime.now();

        this.cancelCompletedDate = LocalDateTime.now();

    }

    /**
     * [비즈니스 로직]
     * 구매자가 주문을 취소함 , 단 주문의 상태가 주문 예약 상태일때만 가능
     */
    public void cancelOrderByBuyer(String cancelReason){

        //검증 로직
        validateReservedByCancel();

        //주문 캔슬
        this.orderStatus=OrderStatus.CANCELLED;

        //취소한 사람 : 구매자
        this.cancelReasonWriter=OrderRole.BUYER;

        //취소 사유
        this.cancelReason=cancelReason;

        //취소 요청 날짜
        this.cancelRequestedDate=LocalDateTime.now();

        //취소 완료 날짜
        this.cancelCompletedDate=LocalDateTime.now();
    }

    /**
     * [비즈니스 로직]
     * 구매자가 주문을 취소요청 함, 단 주문의 상태가 주문확인을 한 상태여야함(아직 상품을 배송하기 전)
     */
    public void cancelRequestByBuyer(String cancelReason){
        //주문의 상태가 주문확인 인지 확인
        validateConfirmedByCancel();

        this.orderStatus = OrderStatus.CANCEL_REQUESTED;

        //취소한 사람 : 구매자
        this.cancelReasonWriter=OrderRole.BUYER;

        //취소 사유
        this.cancelReason=cancelReason;

        //취소 요청 날짜
        this.cancelRequestedDate=LocalDateTime.now();
    }

    /**
     * [비즈니스 로직]
     * 배송 준비 취소
     */
    public void cancelReadyDelivery(){
        this.delivery.cancelReady();
    }

    /**
     * [비즈니스 로직]
     * 구매 확정
     */
    public void completedOrder(){

        //구매 확정 전 검증
        validateCompletedOrder();

        //주문 상태 구매 확정으로 변경
        this.orderStatus=OrderStatus.COMPLETED;

        //배송 완료 처리
        this.delivery.finishDelivery();

        //판매자에게 주문 가격 입금
        this.seller.chargeGMoney(this.totalPrice);

        //에스크로 상태 잠금 해제 (입금 완료)
        this.escrowStatus= EscrowStatus.RELEASED;
        
        //상품 상태 판매 완료로 변경
        this.item.changeSaleStatus(SaleStatus.SOLD_OUT);

        //상점의 거래한 수 1 증가
        this.item.getStore().plusTradeCount();
    }

    /**
     * [비즈니스 로직]
     * 환불 요청
     * @param refundReason 환불 사유
     */
    public void refundRequest(String refundReason){

        this.orderStatus=OrderStatus.REFUND_REQUESTED;

        //배송은 완료 한것이니 배송 완료 처리
        this.delivery.finishDelivery();

        this.cancelReason = refundReason;

        this.cancelReasonWriter=OrderRole.BUYER;

        this.cancelRequestedDate=LocalDateTime.now();
    }

    /**
     * [비즈니스 로직]
     * 환불 요청 승인
     */
    public void acceptRefundRequest(){
        //환불 요청 승인 검증 로직
        validateAcceptRefundRequest();

        this.orderStatus=OrderStatus.REFUND_APPROVED;
    }

    /**
     * [비즈니스 로직]
     * 환불 완료
     */
    public void refundComplete(){

        //환불 완료 처리 검증 로직
        validateRefundComplete();

        this.orderStatus=OrderStatus.REFUNDED;
        this.delivery.finishDelivery();

        this.seller.chargeGMoney(this.deliveryPrice);

        this.buyer.chargeGMoney(this.paidPrice-this.deliveryPrice);
        this.buyer.chargeGPoint(this.usedPoint);

        this.escrowStatus=EscrowStatus.RELEASED;

        this.cancelCompletedDate=LocalDateTime.now();

    }

    //==환불 완료 처리 검증 로직==//
    private void validateRefundComplete() {
        if(!orderStatus.equals(OrderStatus.REFUND_SHIPPED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_COMPLETE_REFUND);
        }
    }

    //==환불 요청 승인 검증 로직==//
    private void validateAcceptRefundRequest() {
        if(!this.orderStatus.equals(OrderStatus.REFUND_REQUESTED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_ACCEPT_REFUND_REQUEST);
        }
    }


    //==구매 확정 전 검증 로직==//
    private void validateCompletedOrder() {
        if(!this.orderStatus.equals(OrderStatus.SHIPPED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_COMPLETE_ORDER);
        }
    }

    //==구매자의 결제 데이터 복구 로직==//
    private void markCanceled() {
        this.escrowStatus=EscrowStatus.CANCELED;

        this.item.changeSaleStatus(SaleStatus.SALE);
    }

    //==구매 취소 상태 처리==//
    private void recoveryBuyerPayment() {
        this.buyer.chargeGMoney(this.paidPrice);

        this.buyer.chargeGPoint(this.usedPoint);
    }


    //==구매자 금액 처리로직==//
    private void deductBuyerPayment() {
        //구매자의 건머니 차감
        this.buyer.deductGMoney(this.paidPrice);

        //구매자의 건포인트 차감
        this.buyer.deductGPoint(this.usedPoint);
    }

    //==구매 확인 상태 처리 로직==//
    private void markConfirmed() {
        //주문 상태 변경 (주문 확인)
        this.orderStatus=OrderStatus.CONFIRMED;

        //에스크로 상태 홀딩 처리 -> 구매자의 돈이 빠져나가고 에스크로에 묶여있는 상태.
        this.escrowStatus=EscrowStatus.HOLDING;

        //상품 주문 상태 -> 예약중
        this.item.changeSaleStatus(SaleStatus.RESERVED);
    }


    //==주문 예약 상태 검증 로직==//
    private void validateReservedByConfirm() {
        if (!this.orderStatus.equals(OrderStatus.RESERVED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_CONFIRM_ORDER);
        }
    }

    //==주문 상태가 주문 예약 상태인지 검증하는 로직==//
    private void validateReservedByCancel() {
        if (!this.orderStatus.equals(OrderStatus.RESERVED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_CANCEL_ORDER);
        }
    }

    //== 주문 상태가 주문확인 상태인지 검증하는 로직==//
    private void validateConfirmedByCancel() {
        if(!this.orderStatus.equals(OrderStatus.CONFIRMED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_CANCEL_ORDER);
        }
    }


    //==주문 상태가 주문취소 요청 상태인지 검증하는 로직==//
    private void validateCancelRequestByCancel() {
        if(!this.orderStatus.equals(OrderStatus.CANCEL_REQUESTED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_CANCEL_ORDER);
        }
    }

    //==배송 : 주문의 상태가 주문확인 인지 검증하는 로직==//
    private void validateConfirmedByDelivery(){
        if(!this.orderStatus.equals(OrderStatus.CONFIRMED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_SHIP_DELIVERY);
        }
    }


}
