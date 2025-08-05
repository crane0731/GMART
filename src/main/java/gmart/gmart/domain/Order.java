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

    @org.hibernate.annotations.Comment("에스크로 상태")
    @Column(name = "escrow_status")
    @Enumerated(EnumType.STRING)
    private EscrowStatus escrowStatus;

    @org.hibernate.annotations.Comment("리뷰 상태")
    @Column(name = "review_status")
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

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

        item.changeSaleStatus(SaleStatus.SOLD_OUT);

        Order order = new Order();

        order.setBuyer(buyer);
        order.setSeller(seller);

        order.item = item;


        order.orderStatus=OrderStatus.REQUESTED;

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
        order.reviewStatus=ReviewStatus.NOT_REVIEWED;
        order.deleteStatus = DeleteStatus.UNDELETED;

        return order;
    }

    /**
     * [연관관계 편의 메서드]
     * Order<->Delivery
     * @param delivery 배송 엔티티
     */
    public void setDelivery(Delivery delivery){

        this.delivery=delivery;
        delivery.setOrder(this);

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

        if(usedPoint!=0) {
            if (usedPoint < 1000) {
                throw new OrderCustomException(ErrorMessage.POINT_MINIMUM_REQUIRED);
            } else if (usedPoint > totalPrice) {
                throw new OrderCustomException(ErrorMessage.POINT_EXCEEDS_TOTAL);
            } else {
                this.paidPrice = totalPrice - usedPoint;
                return this.paidPrice;

            }
        }else {
            this.paidPrice = totalPrice - usedPoint;
            return totalPrice;
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
     * 리뷰상태를 리뷰완료 상태로 변경
     */
    public void reviewedOrder(){
        this.reviewStatus=ReviewStatus.REVIEWED;
    }


    /**
     * [비즈니스 로직]
     * 구매 요청 취소
     */
    public void cancelOrder(){
        //주문이 구매 요청 상태인지 확인
        if(!this.getOrderStatus().equals(OrderStatus.REQUESTED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_CANCEL_ORDER);
        }

        //주문 취소 처리
        this.orderStatus=OrderStatus.CANCELED;

        //상품 SOLD_OUT -> SALE 처리
        this.item.changeSaleStatus(SaleStatus.SALE);

    }

    /**
     * [비즈니스 로직]
     * 주문 거절 처리
     */
    public void rejectOrder(){

        //주문이 구매 요청 상태인지 확인
        if(!this.getOrderStatus().equals(OrderStatus.REQUESTED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_REJECT_ORDER);
        }

        //주문 거절 처리
        this.orderStatus=OrderStatus.REJECT;

        //상품 SOLD_OUT -> SALE 처리
        this.item.changeSaleStatus(SaleStatus.SALE);

    }

    /**
     * [비즈니스 로직]
     * 주문 수락 처리
     * 구매자의 결제대금 만큼 건머니와 건포인트가 차감되며 에스크로 홀딩 상태로 변경
     */
    public void acceptOrder(){

        //주문이 구매 요청 상태인지 확인
        if(!this.getOrderStatus().equals(OrderStatus.REQUESTED)){
            throw new OrderCustomException(ErrorMessage.CANNOT_ACCEPT_ORDER);
        }

        //주문 확인 처리
        this.orderStatus=OrderStatus.ACCEPT;

        //에스크로 결제 대금 홀딩 처리
        escrowHolding();


    }

    /**
     * [비즈니스 로직]
     * 주문 구매 확정 처리
     * 판매자에게 결제대금이 입금되며 에스크로 해제 상태로 변경
     * 판매자의 상점 거래 수 1 증가
     */
    public Long confirmOrder(){

        //주문이 구매 확인 상태인지 확인
        if(!this.getOrderStatus().equals(OrderStatus.ACCEPT)){
            throw new OrderCustomException(ErrorMessage.CANNOT_CONFIRM_ORDER);
        }

        //구매 확정 처리
        this.orderStatus=OrderStatus.CONFIRM;

        //판매자 상점의 거래 수 증가
        this.item.getStore().plusTradeCount();

        //배송 완료 처리
        this.delivery.finishDelivery();

        //에스크로 결제 대금 해제 처리
        escrowReleased();

        //상품 가격의 5프로 만큼 구매자의 포인트 충전
        Long chargePoint = this.itemPrice * 5 / 100;
        this.buyer.chargeGPoint(chargePoint);

        return chargePoint;

    }

    //==에스크로 결제 대금 해제 처리 로직==//
    private void escrowReleased() {
        //판매자의 건 머니 충전
        this.seller.chargeGMoney(this.totalPrice);

        //에스크로 상태를 해제로 변경 (판매자에게 결제 대금이 입금됨)
        this.escrowStatus=EscrowStatus.RELEASED;
    }

    //=에스크로 결제 대금 홀딩 처리 로직==//
    private void escrowHolding() {
        //구매자의 건 포인트와 건 머니 차감
        this.buyer.deductGMoney(this.paidPrice);
        this.buyer.deductGPoint(this.usedPoint);

        //에스크로 상태를 홀딩으로 변경 (에스크로에 결제 대금이 저장됨)
        this.escrowStatus=EscrowStatus.HOLDING;
    }


}
