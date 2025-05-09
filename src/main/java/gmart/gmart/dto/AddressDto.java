package gmart.gmart.dto;

import gmart.gmart.domain.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 주소 DTO
 */
@Getter
@Setter
public class AddressDto {

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address; //주소

    @NotBlank(message = "상세주소를 입력해 주세요.")
    private String addressDetail; //상세주소

    @NotBlank(message = "우편번호를 입력해 주세요.")
    private String zipCode; //우편번호

    /**
     * 생성 메서드
     */
    public static AddressDto createDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setAddress(address.getAddress());
        dto.setAddressDetail(address.getAddressDetails());
        dto.setZipCode(address.getZipCode());
        return dto;
    }

}
