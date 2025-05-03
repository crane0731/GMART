package gmart.gmart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 내장 타입 : 주소
 */

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Comment("주소")
    @Column(name = "address",nullable = false)
    private String address;

    @Comment("상세 주소")
    @Column(name = "address_details",nullable = false)
    private String addressDetails;

    @Comment("우편번호")
    @Column(name = "zipcode",nullable = false)
    private String zipCode;

}
