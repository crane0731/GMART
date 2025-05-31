package gmart.gmart.dto.reportarticle;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 신고 등록 요청 DTO
 */
@Getter
@Setter
public class CreateReportArticleRequestDto {

    @NotBlank(message = "신고 사유를 작성해주세요.")
    private String reason; //신고 사유

}
