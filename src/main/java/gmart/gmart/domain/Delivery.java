package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.DeliveryStatus;
import gmart.gmart.domain.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 배송
 */
@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @org.hibernate.annotations.Comment("배송 아이디")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @org.hibernate.annotations.Comment("받는 사람 이름")
    @Column(name = "sender_name")
    private String senderName;

    @org.hibernate.annotations.Comment("받는 사람 전화번호")
    @Column(name = "sender_phone")
    private String senderPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zipcode")),
            @AttributeOverride(name = "address", column = @Column(name = "sender_address")),
            @AttributeOverride(name = "addressDetails", column = @Column(name = "sender_address_datails"))
    })
    private Address senderAddress;

    @org.hibernate.annotations.Comment("받는 사람 이름")
    @Column(name = "receiver_name")
    private String receiverName;

    @org.hibernate.annotations.Comment("받는 사람 전화번호")
    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "receiver_zipcode")),
            @AttributeOverride(name = "address", column = @Column(name = "receiver_address")),
            @AttributeOverride(name = "addressDetails", column = @Column(name = "receiver_address_details"))
    })
    private Address receiverAddress;

    @org.hibernate.annotations.Comment("배송 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @org.hibernate.annotations.Comment("환불 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order;

}
