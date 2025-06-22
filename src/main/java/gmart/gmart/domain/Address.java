package gmart.gmart.domain;

import gmart.gmart.dto.AddressDto;
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
    @Column(name = "address")
    private String address;

    @Comment("상세 주소")
    @Column(name = "address_details")
    private String addressDetails;

    @Comment("우편번호")
    @Column(name = "zipcode")
    private String zipCode;

    /**
     * 생성 메서드
     * @param dto
     * @return
     */
    public static Address createEntity(AddressDto dto){
        Address entity = new Address();
        entity.address = dto.getAddress();
        entity.addressDetails= dto.getAddressDetail();
        entity.zipCode = dto.getZipCode();
        return entity;

    }

}
