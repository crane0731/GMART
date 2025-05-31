package gmart.gmart.dto.article;

import gmart.gmart.dto.enums.ArticleSortType;
import gmart.gmart.dto.enums.CreateDateSortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 리스트 조회를 위한 조건 DTO
 */
@Getter
@Setter
public class SearchArticleCondDto {

    private String title; //제목
    private String nickname; //작성자 닉네임

    private ArticleSortType sortType; //게시글 정렬 타입

}
