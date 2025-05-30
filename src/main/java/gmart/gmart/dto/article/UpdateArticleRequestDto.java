package gmart.gmart.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글 수정 요청 DTO
 */
@Getter
@Setter
public class UpdateArticleRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "널 일수 없습니다. 빈 리스트값을 넣어주세요.")
    private List<String> imageUrls;

}
