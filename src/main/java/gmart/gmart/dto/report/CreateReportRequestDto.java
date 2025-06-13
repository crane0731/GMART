package gmart.gmart.dto.report;

import gmart.gmart.domain.enums.ReporterRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 신고 요청 DTO
 */
@Getter
@Setter
public class CreateReportRequestDto {

    @NotNull(message = "피신고자 아이디를 입력해주세요")
    private Long reportedMemberId;//신고할 회원 아이디

    @NotNull(message = "신고 대상 상품 아이디를 입력해주세요")
    private Long itemId; //신고할 상품 아이디

    @NotNull(message = "신고자의 역할을 입력해 주세요 [BUYER/SELLER]")
    private ReporterRole reporterRole;//신고자 역할

    @NotBlank(message = "신고 사유를 작성해주세요.")
    private String reason; //신고 사유

}
