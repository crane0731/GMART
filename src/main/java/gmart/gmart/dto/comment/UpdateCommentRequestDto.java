package gmart.gmart.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateCommentRequestDto {

    @NotBlank(message = "댓글을 입력해 주세요.")
    private String content; //댓글 내용

}
