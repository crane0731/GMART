package gmart.gmart.domain;


import gmart.gmart.domain.baseentity.BaseTimeEntity;
import gmart.gmart.domain.enums.CouponActiveStatus;
import gmart.gmart.domain.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 쿠폰 정보
 */
@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Comment("쿠폰 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("쿠폰 코드")
    @Column(name = "code", nullable = false)
    private String code;

    @Comment("설명")
    @Column(name = "description", nullable = false)
    private String description;

    @Comment("할인 타입")
    @Column(name ="discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Comment("할인 값")
    @Column(name = "discount_value", nullable = false)
    private Long discountValue=0L;

    @Comment("최소 주문 금액")
    @Column(name = "min_order_price")
    private Long minOrderPrice=0L;

    @Comment("만료일")
    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Comment("쿠폰 사용 가능 상태")
    @Column(name = "active_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponActiveStatus activeStatus;


}
