package gmart.gmart.dto.reportitem;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 신고 요청 DTO
 */
@Getter
@Setter
public class CreateReportItemRequestDto {

    @NotBlank(message = "신고 사유를 작성해주세요.")
    private String reason; //신고 사유

}
