package gmart.gmart.domain;

import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(name = "member_name")
    private String memberName;

    @org.hibernate.annotations.Comment("받는 사람 전화번호")
    @Column(name = "member_phone")
    private String memberPhone;

    @Embedded
    private Address address;

    @org.hibernate.annotations.Comment("배송 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order=new Order();

}
