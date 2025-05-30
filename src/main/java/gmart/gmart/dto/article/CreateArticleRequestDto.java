package gmart.gmart.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글 등록 요청 DTO
 */
@Getter
@Setter
public class CreateArticleRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private List<String> imageUrls;
}
