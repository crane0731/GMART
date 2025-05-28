package gmart.gmart.dto.inquiry;

import lombok.Getter;
import lombok.Setter;

/**
 * 문의 수정 DTO
 */
@Getter
@Setter
public class UpdateInquiryRequestDto {

    private String title;
    private String content;

}
