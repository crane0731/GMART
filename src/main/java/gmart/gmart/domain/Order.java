package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseAuditingEntity;
import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.EscrowStatus;
import gmart.gmart.domain.enums.OrderStatus;
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

}
