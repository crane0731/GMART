package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeleteStatus;
import gmart.gmart.domain.enums.DeliveryStatus;
import gmart.gmart.domain.enums.RefundStatus;
import gmart.gmart.exception.ErrorMessage;
import gmart.gmart.exception.OrderCustomException;
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

    @org.hibernate.annotations.Comment("송장 번호")
    @Column(name = "tracking_number")
    private String trackingNumber;

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

    @org.hibernate.annotations.Comment("환불 송장 번호")
    @Column(name = "refund_tracking_number")
    private String refundTrackingNumber;


    @org.hibernate.annotations.Comment("삭제 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status")
    private DeleteStatus deleteStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order;


    /**
     * [생성 메서드]
     * @param sender 보내는 회원
     * @param receiver 받는 회원
     * @param refundStatus 환불 상태
     * @return Delivery 배송 엔티티
     */
    public static Delivery create(Member sender , Member receiver, RefundStatus refundStatus) {

        Delivery delivery = new Delivery();

        delivery.senderName=sender.getName();
        delivery.senderPhone= sender.getPhoneNumber();
        delivery.senderAddress=sender.getAddress();

        delivery.receiverName=receiver.getName();
        delivery.receiverPhone= receiver.getPhoneNumber();
        delivery.receiverAddress=receiver.getAddress();

        delivery.deliveryStatus=DeliveryStatus.READY;

        delivery.refundStatus=refundStatus;

        delivery.deleteStatus=DeleteStatus.UNDELETED;

        return delivery;
    }

    /**
     * [Setter]
     * @param order 주문 엔티티
     */
    protected void setOrder(Order order) {
        this.order = order;
    }


    /**
     * [비즈니스 로직]
     * 송장 번호 세팅
     * @param trackingNumber 송장 번호
     */
    protected void setTrackingNumber(String trackingNumber){
        this.trackingNumber=trackingNumber;

        this.deliveryStatus=DeliveryStatus.SHIPPING;

        this.refundStatus=RefundStatus.REFUND;
    }

    /**
     * [비즈니스 로직]
     * 환불 - 송장번호 세팅
     * @param refundTrackingNumber
     */
    protected void setRefundTrackingNumber(String refundTrackingNumber){
        this.refundTrackingNumber=refundTrackingNumber;
        this.deliveryStatus=DeliveryStatus.SHIPPING;
    }

    /**
     * [비즈니스 로직]
     * 배송 준비 완료
     */
    public void ready(){
        this.deliveryStatus=DeliveryStatus.READY;
    }

    /**
     * [비즈니스 로직]
     * 배송 준비 취소
     */
    protected void cancelReady(){
        //검증 로직
        validateCancelReady();
        this.deliveryStatus = DeliveryStatus.CANCELED;
    }

    /**
     * [비즈니스 로직]
     * 배송 완료
     */
    protected void finishDelivery(){
        this.deliveryStatus=DeliveryStatus.DELIVERED;
    }


    //==배송 준비 취소 검증 로직==//
    private void validateCancelReady() {
        if(!deliveryStatus.equals(DeliveryStatus.READY)){
            throw new OrderCustomException(ErrorMessage.CANNOT_CANCEL_READY_DELIVERY);
        }
    }

}
