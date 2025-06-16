package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.EscrowStatus;
import gmart.gmart.domain.enums.OrderStatus;
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

    @org.hibernate.annotations.Comment("취소,환불 요청 일자")
    @Column(name = "cancel_requested_date")
    private LocalDateTime cancelRequestedDate;

    @org.hibernate.annotations.Comment("취소,환불 처리 완료 일자")
    @Column(name = "cancel_completed_date")
    private LocalDateTime cancelCompletedAt;

    @org.hibernate.annotations.Comment("에스크로 상태")
    @Column(name = "escrow_status")
    @Enumerated(EnumType.STRING)
    private EscrowStatus escrowStatus;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;


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
     * @param buyer
     */
    public void setBuyer(Member buyer) {
        this.buyer = buyer;
        buyer.getBuyOrders().add(this);
    }

    /**
     * [연관관계 편의 메서드]
     * @param seller
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
        validateConfirmable();
        
        //구매자의 결제 금액 처리
        deductBuyerPayment();

        //구매 확인 상태 처리
        markConfirmed();
    }


    /**
     * [비즈니스 로직]
     * 판매자가 주문 거절 처리
     */
    public void cancelOrder(){
        if (this.orderStatus.equals(OrderStatus.RESERVED)) {
            this.orderStatus=OrderStatus.CANCELLED;
        }
    }

    //==주문 확인 처리 검증 로직==//
    private void validateConfirmable() {
        if (!this.orderStatus.equals(OrderStatus.RESERVED)) {
            throw new OrderCustomException(ErrorMessage.CANNOT_CONFIRM_ORDER);
        }
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
        this.item.reservedSaleStatus();
    }

}
