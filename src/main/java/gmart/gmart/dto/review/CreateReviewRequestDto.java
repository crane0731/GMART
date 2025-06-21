package gmart.gmart.dto.review;

import gmart.gmart.domain.enums.ReviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 생성 요청 DTO
 */
@Getter
@Setter
public class CreateReviewRequestDto {

    @NotNull(message = "주문 아이디를 입력해주요.")
    private Long orderId; //주문 아이디

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content; //리뷰 내용

    @NotNull(message = "[GOOD/BAD] 를 입력해주세요")
    private ReviewType reviewType; //리뷰 타입

    @NotNull(message = "점수를 입력해 주세요")
    private Long rating; //점수
}
