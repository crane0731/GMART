package gmart.gmart.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 생성 요청 DTO
 */
@Getter
@Setter
public class CreateCommentRequestDto {

    private Long parentCommentId; //부모 댓글 아이디 , 없으면 null 입력

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content; //댓글 내용

}

