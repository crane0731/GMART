package gmart.gmart.dto;

import gmart.gmart.domain.MemberCoupon;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 회원쿠폰 DTO
 */
@Getter
@Setter
public class MemberCouponDto {

    private Long couponId; //쿠폰 아이디

    private String couponName; //쿠폰 이름

    private Long count; //수량

    private String description; //설명

    private Long minOrderPrice; //최소 주문 금액

    private LocalDateTime expireDate; //만료일


    /**
     * 생성 메서드
     */
    public static MemberCouponDto createDto(MemberCoupon memberCoupon) {
        MemberCouponDto dto = new MemberCouponDto();
        dto.couponId=memberCoupon.getCoupon().getId();
        dto.couponName=memberCoupon.getCoupon().getName();
        dto.count=memberCoupon.getCount();
        dto.description=memberCoupon.getCoupon().getDescription();
        dto.minOrderPrice=memberCoupon.getCoupon().getMinOrderPrice();
        dto.expireDate=memberCoupon.getCoupon().getExpireDate();

        return dto;
    }

}
